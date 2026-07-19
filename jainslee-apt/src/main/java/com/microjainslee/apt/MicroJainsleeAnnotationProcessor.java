/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.apt;

import com.microjainslee.api.annotations.DeployableUnit;
import com.microjainslee.api.annotations.EventType;
import com.microjainslee.api.annotations.SbbAnnotation;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Javac annotation processor for micro-jainslee Phase 3.5.
 *
 * <p>Scans the compilation unit for the three supported annotations
 * ({@link SbbAnnotation}, {@link DeployableUnit}, {@link EventType}) and
 * writes a {@code META-INF/microjainslee/sbb-index.properties} file the
 * runtime container reads on startup, and a {@code GeneratedEventTypes.java}
 * source file with compile-time {@code EventTypeRef} constants.</p>
 *
 * <p>The properties file uses simple flat keys so it can be loaded with a
 * vanilla {@link Properties} instance:</p>
 * <pre>
 * sbb.0.class=com.foo.MySbb
 * sbb.0.name=MySbb
 * sbb.0.vendor=com.microjainslee
 * sbb.0.version=1.0
 * sbb.1.class=...
 * eventType.0.class=com.foo.MyEvent
 * du.0.class=com.foo.MyDU
 * du.0.sbbs=com.foo.MySbb,com.foo.OtherSbb
 * </pre>
 *
 * <p>This processor runs in the same {@code javac} invocation as the
 * project under build — it never reads or writes bytecode, only metadata.</p>
 */
@SupportedAnnotationTypes({
        "com.microjainslee.api.annotations.SbbAnnotation",
        "com.microjainslee.api.annotations.DeployableUnit",
        "com.microjainslee.api.annotations.EventType"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MicroJainsleeAnnotationProcessor extends AbstractProcessor {

    // Diagnostics go through javac's own Messager — an annotation processor
    // must NOT touch a logging framework: the processor path only carries the
    // processor jar + its compile deps, so a log4j static initializer here
    // dies with NoClassDefFoundError ("Processor could not be instantiated").
    private static final String OUTPUT_RESOURCE = "META-INF/microjainslee/sbb-index.properties";

    // Accumulator state — survives across rounds because the JDK may invoke process() more than once
    // (round 1 with all annotated source, round 2 = the synthetic "last round" with no inputs).
    private final List<SbbInfo> sbbs = new ArrayList<SbbInfo>();
    private final List<EventTypeInfo> events = new ArrayList<EventTypeInfo>();
    private final List<DuInfo> dus = new ArrayList<DuInfo>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // CRITICAL: do NOT claim the annotations (return false). Other processors may want them too.
        // We just observe them, accumulate state in the lists, and write the index file once
        // on the final round via processingOver().

        for (Element e : roundEnv.getElementsAnnotatedWith(SbbAnnotation.class)) {
            if (!(e instanceof TypeElement)) {
                continue;
            }
            SbbAnnotation a = e.getAnnotation(SbbAnnotation.class);
            sbbs.add(new SbbInfo(((TypeElement) e).getQualifiedName().toString(),
                    a.name(), a.vendor(), a.version()));
            note("Discovered @SbbAnnotation " + ((TypeElement) e).getQualifiedName()
                    + " (name='" + a.name() + "', vendor='" + a.vendor()
                    + "', version='" + a.version() + "')");
        }

        for (Element e : roundEnv.getElementsAnnotatedWith(EventType.class)) {
            if (!(e instanceof TypeElement)) {
                continue;
            }
            EventType a = e.getAnnotation(EventType.class);
            events.add(new EventTypeInfo(((TypeElement) e).getQualifiedName().toString(),
                    a.name(), a.vendor(), a.version()));
            note("Discovered @EventType " + ((TypeElement) e).getQualifiedName()
                    + " (name='" + a.name() + "')");
        }

        for (Element e : roundEnv.getElementsAnnotatedWith(DeployableUnit.class)) {
            if (!(e instanceof TypeElement)) {
                continue;
            }
            TypeElement te = (TypeElement) e;
            // Class[] members cannot be read via reflection inside an APT — go through AnnotationMirror.
            AnnotationMirror am = findAnnotationMirror(te, DeployableUnit.class);
            String name = stringValue(am, "name");
            String vendor = stringValue(am, "vendor");
            String version = stringValue(am, "version");
            String sbbsCsv = typeArrayCsv(am, "sbbs");
            String rasCsv = typeArrayCsv(am, "ras");
            String profilesCsv = typeArrayCsv(am, "profileSpecs");
            dus.add(new DuInfo(te.getQualifiedName().toString(),
                    name, vendor, version,
                    sbbsCsv, rasCsv, profilesCsv));
            note("Discovered @DeployableUnit " + te.getQualifiedName()
                    + " (name='" + name + "', vendor='" + vendor + "', version='" + version
                    + "', sbbs=" + sbbsCsv + ", ras=" + rasCsv + ", profiles=" + profilesCsv + ")");
        }

        if (roundEnv.processingOver()) {
            try {
                writeIndexFile(sbbs, events, dus);
                writeGeneratedEventTypes(events);
            } catch (IOException ioe) {
                processingEnv.getMessager().printMessage(javax.tools.Diagnostic.Kind.ERROR,
                        "Failed to write micro-jainslee generated metadata: " + ioe.getMessage());
                throw new RuntimeException("Could not write micro-jainslee generated metadata", ioe);
            }
        }
        // Don't claim — let other processors process these annotations too.
        return false;
    }

    private void writeIndexFile(List<SbbInfo> sbbs,
                                List<EventTypeInfo> events,
                                List<DuInfo> dus) throws IOException {
        Filer filer = processingEnv.getFiler();
        Properties props = new Properties();

        // Merge with any existing file (so multi-round compilation preserves earlier entries).
        try {
            FileObject existing = filer.getResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_RESOURCE);
            try (InputStream in = existing.openInputStream()) {
                props.load(in);
            }
        } catch (Exception notFound) {
            // First round in this output location — nothing to merge with.
        }

        // Determine next free index for each section by scanning existing keys.
        int sbbBase = nextIndex(props, "sbb.");
        int eventBase = nextIndex(props, "eventType.");
        int duBase = nextIndex(props, "du.");

        for (int i = 0; i < sbbs.size(); i++) {
            SbbInfo s = sbbs.get(i);
            int n = sbbBase + i;
            props.setProperty("sbb." + n + ".class", s.fqn);
            props.setProperty("sbb." + n + ".name", s.name);
            props.setProperty("sbb." + n + ".vendor", s.vendor);
            props.setProperty("sbb." + n + ".version", s.version);
        }
        for (int i = 0; i < events.size(); i++) {
            EventTypeInfo ev = events.get(i);
            int n = eventBase + i;
            props.setProperty("eventType." + n + ".class", ev.fqn);
            props.setProperty("eventType." + n + ".name", ev.name);
            props.setProperty("eventType." + n + ".vendor", ev.vendor);
            props.setProperty("eventType." + n + ".version", ev.version);
        }
        for (int i = 0; i < dus.size(); i++) {
            DuInfo d = dus.get(i);
            int n = duBase + i;
            props.setProperty("du." + n + ".class", d.fqn);
            props.setProperty("du." + n + ".name", d.name);
            props.setProperty("du." + n + ".vendor", d.vendor);
            props.setProperty("du." + n + ".version", d.version);
            if (!d.sbbsCsv.isEmpty()) props.setProperty("du." + n + ".sbbs", d.sbbsCsv);
            if (!d.rasCsv.isEmpty())   props.setProperty("du." + n + ".ras",   d.rasCsv);
            if (!d.profilesCsv.isEmpty()) props.setProperty("du." + n + ".profileSpecs", d.profilesCsv);
        }

        FileObject out = filer.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_RESOURCE);
        try (OutputStream os = out.openOutputStream()) {
            props.store(os, "micro-jainslee index — generated by MicroJainsleeAnnotationProcessor");
        }
        note("Wrote " + OUTPUT_RESOURCE + ": " + sbbs.size() + " sbb(s), "
                + events.size() + " eventType(s), " + dus.size() + " du(s)");
    }

    /** javac diagnostics channel — the only sanctioned output for a processor. */
    private void note(String message) {
        processingEnv.getMessager().printMessage(
                javax.tools.Diagnostic.Kind.NOTE, "[micro-jainslee] " + message);
    }

    /**
     * Emit {@code GeneratedEventTypes.java} with one {@code EventTypeRef} constant per
     * discovered {@link EventType}. Uses the package of the first annotated event type.
     */
    private void writeGeneratedEventTypes(List<EventTypeInfo> events) throws IOException {
        if (events.isEmpty()) {
            return;
        }

        String pkg = packageName(events.get(0).fqn);
        String simpleName = "GeneratedEventTypes";
        String fqcn = pkg.isEmpty() ? simpleName : pkg + "." + simpleName;

        Filer filer = processingEnv.getFiler();
        JavaFileObject source = filer.createSourceFile(fqcn);
        StringBuilder body = new StringBuilder();
        body.append("/*\n");
        body.append(" * micro-jainslee 1.1.0 — generated by MicroJainsleeAnnotationProcessor.\n");
        body.append(" *\n");
        body.append(" * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).\n");
        body.append(" * See the LICENSE file at the root of this repository for the full text.\n");
        body.append(" *\n");
        body.append(" * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.\n");
        body.append(" * Contact: nhanth87@gmail.com\n");
        body.append(" */\n\n");
        if (!pkg.isEmpty()) {
            body.append("package ").append(pkg).append(";\n\n");
        }
        body.append("import com.microjainslee.api.EventTypeRef;\n\n");
        body.append("/**\n");
        body.append(" * Compile-time event type constants generated from {@code @EventType} annotations.\n");
        body.append(" */\n");
        body.append("public final class ").append(simpleName).append(" {\n\n");
        body.append("    private ").append(simpleName).append("() {\n");
        body.append("    }\n\n");

        for (EventTypeInfo event : events) {
            String constant = toConstantName(event.name);
            body.append("    /** {@code ").append(event.fqn).append("} — ")
                    .append(event.name).append(" / ").append(event.vendor)
                    .append(" / ").append(event.version).append(" */\n");
            body.append("    public static final EventTypeRef ")
                    .append(constant).append(" = new EventTypeRef(")
                    .append(stringLiteral(event.name)).append(", ")
                    .append(stringLiteral(event.vendor)).append(", ")
                    .append(stringLiteral(event.version)).append(");\n\n");
        }
        body.append("}\n");

        try (Writer writer = source.openWriter()) {
            writer.write(body.toString());
        }
        note("Wrote " + fqcn + " with " + events.size() + " event type constant(s)");
    }

    private static String packageName(String fqn) {
        int dot = fqn.lastIndexOf('.');
        return dot >= 0 ? fqn.substring(0, dot) : "";
    }

    private static String toConstantName(String name) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c) && i > 0
                    && Character.isLowerCase(name.charAt(i - 1))) {
                sb.append('_');
            }
            sb.append(Character.toUpperCase(c));
        }
        return sb.toString();
    }

    private static String stringLiteral(String value) {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private static AnnotationMirror findAnnotationMirror(TypeElement te, Class<?> annotationClass) {
        String fqn = annotationClass.getCanonicalName();
        for (AnnotationMirror m : te.getAnnotationMirrors()) {
            if (m.getAnnotationType().toString().equals(fqn)) {
                return m;
            }
        }
        return null;
    }

    private static String stringValue(AnnotationMirror mirror, String key) {
        if (mirror == null) return "";
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry
                : mirror.getElementValues().entrySet()) {
            if (entry.getKey().getSimpleName().contentEquals(key)) {
                Object v = entry.getValue().getValue();
                return v == null ? "" : v.toString();
            }
        }
        return "";
    }

    /**
     * Read an array-valued annotation member like {@code Class<?>[] sbbs()} as a comma-separated
     * string of FQNs. Each array element is a {@link TypeMirror}; resolve to a string via
     * {@code toString()} (works for declared types and Type.Class cases alike inside an APT).
     */
    private static String typeArrayCsv(AnnotationMirror mirror, String key) {
        if (mirror == null) return "";
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry
                : mirror.getElementValues().entrySet()) {
            if (!entry.getKey().getSimpleName().contentEquals(key)) {
                continue;
            }
            Object raw = entry.getValue().getValue();
            if (!(raw instanceof List)) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (Object item : (List<?>) raw) {
                if (item instanceof TypeMirror) {
                    if (sb.length() > 0) sb.append(',');
                    sb.append(item.toString());
                }
            }
            return sb.toString();
        }
        return "";
    }

    private static int nextIndex(Properties props, String prefix) {
        int max = -1;
        for (String key : props.stringPropertyNames()) {
            if (key.startsWith(prefix)) {
                int dot = key.indexOf('.', prefix.length());
                if (dot > 0) {
                    try {
                        int n = Integer.parseInt(key.substring(prefix.length(), dot));
                        if (n > max) max = n;
                    } catch (NumberFormatException ignored) {
                        // not an index — skip
                    }
                }
            }
        }
        return max + 1;
    }

    private static final class SbbInfo {
        final String fqn, name, vendor, version;
        SbbInfo(String fqn, String name, String vendor, String version) {
            this.fqn = fqn; this.name = name; this.vendor = vendor; this.version = version;
        }
    }

    private static final class EventTypeInfo {
        final String fqn, name, vendor, version;
        EventTypeInfo(String fqn, String name, String vendor, String version) {
            this.fqn = fqn; this.name = name; this.vendor = vendor; this.version = version;
        }
    }

    private static final class DuInfo {
        final String fqn, name, vendor, version, sbbsCsv, rasCsv, profilesCsv;
        DuInfo(String fqn, String name, String vendor, String version,
               String sbbsCsv, String rasCsv, String profilesCsv) {
            this.fqn = fqn; this.name = name; this.vendor = vendor; this.version = version;
            this.sbbsCsv = sbbsCsv; this.rasCsv = rasCsv; this.profilesCsv = profilesCsv;
        }
    }
}
