/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.codegen;

import com.microjainslee.api.Sbb;
import com.microjainslee.api.annotations.OffHeap;
import com.microjainslee.core.CmpFieldStore;
import com.microjainslee.core.CmpFieldStoreLocator;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Deploy-time concrete SBB class generator using Javassist (Solution A).
 *
 * <p>Generates a concrete subclass for each abstract SBB class with:
 * <ul>
 *   <li>CMP backing fields (private, typed, direct field access)</li>
 *   <li>CMP getter/setter implementations that read/write through the
 *       thread-local {@link CmpFieldStoreLocator} so persistence stays
 *       identical to the legacy {@link com.microjainslee.core.CmpAccessorInvoker}
 *       path. No reflection on the hot path: getters/setters are plain
 *       direct method bodies emitted by Javassist.</li>
 *   <li>Stub {@code sbbLoad} / {@code sbbStore} implementations so the
 *       generated class is concrete and instantiable.</li>
 * </ul>
 *
 * <p><b>Solution A</b> from the production roadmap: write the .class to a
 * deploy directory, load via {@link URLClassLoader}, never enter the JVM
 * metaspace through {@code toClass()}.
 *
 * <p><b>Field naming</b>: derived from abstract method name.
 * <pre>{@code
 *   getSessionId     -> sessionId
 *   setSessionId     -> sessionId
 *   getCmpFieldFoo   -> foo
 *   getCmpField      -> cmpField
 *   get              -> _unnamed
 * }</pre>
 *
 * <p><b>Type mapping</b>: the Javassist {@link CtClass#getName()} is used
 * verbatim including primitive names ({@code int}, {@code long},
 * {@code boolean}, ...). Auto-boxing on the SBB call site means
 * {@code public abstract int getCounter();} becomes a getter that
 * un-boxes an {@link Integer} read from the store, so the SBB interface
 * stays primitive-friendly while the store side keeps the boxed
 * representation. This mirrors the
 * {@link com.microjainslee.core.CmpAccessorInvoker} contract.
 *
 * <p><b>Thread-local lookup</b>: each CMP read/write resolves the active
 * {@link CmpFieldStore} via {@link CmpFieldStoreLocator#get()}. When the
 * locator returns {@code null} (no container bound, e.g. tests), the
 * field falls back to a per-instance {@code ConcurrentHashMap} so the
 * generated class is still usable in isolation. The instance map is
 * keyed by the entity id captured at {@code sbbCreate} time via a
 * synthetic helper emitted into every concrete class.
 *
 * <p><b>Module</b>: {@code jainslee-codegen} (NEW in P1.4 / S2).
 * <b>Dependency</b>: {@code org.javassist:javassist:3.30.0-GA}.
 *
 * @author Tran Nhan (nhanth87)
 */
public class ConcreteSbbGenerator {

    private static final Logger LOG = LogManager.getLogger(ConcreteSbbGenerator.class);

    /** Suffix appended to the abstract class name when emitting the concrete class. */
    public static final String CONCRETE_SUFFIX = "$Concrete";

    // Cache: abstract class name -> generated concrete Class (avoid regenerate on hot path).
    private final Map<String, Class<?>> generatedCache = new ConcurrentHashMap<String, Class<?>>();

    // ------------------------------------------------------------------------
    //  Public API
    // ------------------------------------------------------------------------

    /**
     * Generate (or return cached) concrete implementation of the supplied
     * abstract SBB class.
     *
     * @param sbbAbstractClass abstract SBB class - must have at least one
     *                         abstract getXxx / setXxx CMP pair declared
     * @param deployDir        directory to write .class files (created if missing)
     * @return concrete class loaded into the deployment ClassLoader
     */
    public Class<?> getOrGenerate(Class<?> sbbAbstractClass, Path deployDir) throws IOException {
        Objects.requireNonNull(sbbAbstractClass, "sbbAbstractClass is required");
        Objects.requireNonNull(deployDir, "deployDir is required");
        final String name = sbbAbstractClass.getName();
        Class<?> cached = generatedCache.get(name);
        if (cached != null) {
            return cached;
        }
        return generatedCache.computeIfAbsent(name, key -> {
            try {
                return doGenerate(sbbAbstractClass, deployDir);
            } catch (IOException | javassist.CannotCompileException | javassist.NotFoundException e) {
                throw new RuntimeException("Codegen failed for " + key, e);
            }
        });
    }

    /**
     * Derive the backing field name from an abstract CMP method name.
     *
     * @param methodName abstract method name, typically "getSessionId" or "setCounter"
     * @return backing field name, e.g. "sessionId" / "counter"
     */
    public static String toCmpFieldName(String methodName) {
        if (methodName == null) {
            throw new IllegalArgumentException("methodName is required");
        }
        String stripped = methodName;
        if (stripped.startsWith("get") || stripped.startsWith("set")) {
            stripped = stripped.substring(3);
        }
        // Handle the CmpFieldFoo / CmpField cases. When the remainder is
        // exactly "CmpField", keep it as a single word but lowercase the
        // first letter so the backing field is "cmpField" (not "_unnamed").
        if (stripped.startsWith("CmpField")) {
            stripped = stripped.substring(8);
            if (stripped.isEmpty()) {
                return "cmpField";
            }
            return Character.toLowerCase(stripped.charAt(0)) + stripped.substring(1);
        }
        if (stripped.isEmpty()) {
            return "_unnamed";
        }
        return Character.toLowerCase(stripped.charAt(0)) + stripped.substring(1);
    }

    /**
     * Emit a single CMP backing field + getter/setter pair into the supplied
     * concrete {@link CtClass}.
     *
     * <p>This is the canonical primitive the rest of the generator builds on;
     * it is exposed so deploy-time frameworks that want to compose their own
     * concrete builder (e.g. the S4 child-relation wiring) can reuse it
     * without re-implementing the Modifier + body-shaping dance.
     *
     * @param fieldName logical CMP field name (e.g. "counter")
     * @param type      the Javassist field type (primitive or reference)
     * @param concrete  the concrete class to mutate
     * @param getter    the abstract getter method to implement
     * @param setter    the abstract setter method to implement (may be null
     *                  for read-only CMP fields)
     */
    public void generateCmpField(String fieldName, CtClass type, CtClass concrete,
                                 CtMethod getter, CtMethod setter)
            throws javassist.CannotCompileException {
        Objects.requireNonNull(fieldName, "fieldName");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(concrete, "concrete");
        Objects.requireNonNull(getter, "getter");

        // 1. Emit the backing field
        CtField field = new CtField(type, fieldName, concrete);
        field.setModifiers(Modifier.PRIVATE);
        concrete.addField(field);

        // 2. Emit the getter body
        CtMethod getterImpl = CtNewMethod.copy(getter, concrete, null);
        getterImpl.setModifiers(getterImpl.getModifiers() & ~Modifier.ABSTRACT);
        getterImpl.setBody(buildGetterBody(fieldName, type));
        concrete.addMethod(getterImpl);

        // 3. Emit the setter body (optional)
        if (setter != null) {
            CtMethod setterImpl = CtNewMethod.copy(setter, concrete, null);
            setterImpl.setModifiers(setterImpl.getModifiers() & ~Modifier.ABSTRACT);
            setterImpl.setBody(buildSetterBody(fieldName, type));
            concrete.addMethod(setterImpl);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Emitted CMP field {} {}", type.getName(), fieldName);
        }
    }

    // ------------------------------------------------------------------------
    //  Generation pipeline
    // ------------------------------------------------------------------------

    private Class<?> doGenerate(Class<?> sbbAbstractClass, Path deployDir) throws IOException, javassist.CannotCompileException, javassist.NotFoundException {
        if (!Sbb.class.isAssignableFrom(sbbAbstractClass)) {
            throw new IllegalArgumentException(
                    "Source class must implement com.microjainslee.api.Sbb: "
                            + sbbAbstractClass.getName());
        }

        ClassPool pool = new ClassPool(ClassPool.getDefault());
        pool.appendClassPath(new LoaderClassPath(sbbAbstractClass.getClassLoader()));
        // javassist can also pick up CmpFieldStore / CmpFieldStoreLocator from
        // the thread context loader — both ship in jainslee-core which the
        // embedder always brings in.
        ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
        if (ctxLoader != null) {
            pool.appendClassPath(new LoaderClassPath(ctxLoader));
        }

        final String concreteName = sbbAbstractClass.getName() + CONCRETE_SUFFIX;
        final CtClass abstractSbb;
        final CtClass concreteSbb;
        try {
            abstractSbb = pool.get(sbbAbstractClass.getName());
            concreteSbb = pool.makeClass(concreteName, abstractSbb);
        } catch (javassist.NotFoundException e) {
            throw new IOException(
                    "Could not resolve abstract SBB " + sbbAbstractClass.getName(), e);
        }

        OffHeap offHeap = sbbAbstractClass.getAnnotation(OffHeap.class);
        try {
            // 1. Emit synthetic helpers used by the accessor bodies
            emitCmpHelpers(concreteSbb);
            if (offHeap != null) {
                // Off-heap CMP (docs/en/design-offheap-sbb-state.md §5):
                // implement OffHeapBindable + slot-bound accessors.
                emitOffHeapHelpers(concreteSbb, sbbAbstractClass.getName());
            }
            // 2. CMP accessors - primary task
            generateCmpAccessors(concreteSbb, abstractSbb, offHeap);
            // 3. No-arg constructor (explicit for clarity)
            CtConstructor ctor = CtNewConstructor.defaultConstructor(concreteSbb);
            concreteSbb.addConstructor(ctor);
            // 4. sbbLoad / sbbStore stubs
            generateSbbLoadStore(concreteSbb, abstractSbb);
        } catch (javassist.CannotCompileException e) {
            throw new IOException("Failed to compile concrete body for " + concreteName, e);
        }

        // Persist to deployDir (Solution A)
        Files.createDirectories(deployDir);
        concreteSbb.writeFile(deployDir.toString());
        LOG.info("Generated: {} -> {}/{}",
                sbbAbstractClass.getSimpleName(),
                deployDir,
                concreteName.replace('.', '/'));

        // Detach the in-memory CtClass so Javassist releases its ClassPool ref.
        concreteSbb.detach();

        // Load via URLClassLoader so the JVM treats it like any .class on disk.
        return loadFromDeployDir(concreteName, deployDir, sbbAbstractClass.getClassLoader());
    }

    /**
     * Emit two private helpers used by every CMP accessor body:
     * <ul>
     *   <li>{@code Object _cmpRead(String name)} - read field through
     *       {@link CmpFieldStoreLocator}, falling back to the in-memory
     *       backing field when no store is bound (tests, hot-reload).</li>
     *   <li>{@code void _cmpWrite(String name, Object value)} - write
     *       field through {@link CmpFieldStoreLocator}, also fall back.</li>
     * </ul>
     * The entity id is sourced from a synthetic {@code _sbbEntityId}
     * field initialised at {@code sbbCreate()} time.
     */
    private void emitCmpHelpers(CtClass concrete) throws javassist.CannotCompileException, javassist.NotFoundException {
        // Per-instance state: id + fallback map
        CtClass stringType = concrete.getClassPool().get("java.lang.String");
        CtClass mapType = concrete.getClassPool().get("java.util.Map");
        CtClass hashMapType = concrete.getClassPool().get("java.util.HashMap");
        CtClass conMapType = concrete.getClassPool().get("java.util.concurrent.ConcurrentHashMap");

        CtField idField = new CtField(stringType, "_sbbEntityId", concrete);
        idField.setModifiers(Modifier.PRIVATE);
        concrete.addField(idField, CtField.Initializer.byExpr("null"));

        CtField fallbackField = new CtField(conMapType, "_cmpFallback", concrete);
        fallbackField.setModifiers(Modifier.PRIVATE);
        concrete.addField(fallbackField, CtField.Initializer.byExpr("new java.util.concurrent.ConcurrentHashMap()"));

        // _cmpRead(String) -> Object
        CtMethod read = CtNewMethod.make(
                "private Object _cmpRead(String name) {\n"
                + "  com.microjainslee.core.CmpFieldStore s = com.microjainslee.core.CmpFieldStoreLocator.get();\n"
                + "  if (s == null || _sbbEntityId == null) {\n"
                + "    return _cmpFallback.get(name);\n"
                + "  }\n"
                + "  java.util.Map m = s.load(_sbbEntityId);\n"
                + "  return m.containsKey(name) ? m.get(name) : _cmpFallback.get(name);\n"
                + "}",
                concrete);
        concrete.addMethod(read);

        // _cmpWrite(String, Object) -> void
        CtMethod write = CtNewMethod.make(
                "private void _cmpWrite(String name, Object value) {\n"
                + "  com.microjainslee.core.CmpFieldStore s = com.microjainslee.core.CmpFieldStoreLocator.get();\n"
                + "  _cmpFallback.put(name, value);\n"
                + "  if (s == null || _sbbEntityId == null) return;\n"
                + "  java.util.Map m = new java.util.HashMap(s.load(_sbbEntityId));\n"
                + "  m.put(name, value);\n"
                + "  s.store(_sbbEntityId, m);\n"
                + "}",
                concrete);
        concrete.addMethod(write);
    }

    /**
     * Generate backing field + getter + setter for every abstract CMP pair
     * detected on the abstract SBB.
     *
     * <p>Pattern: {@code abstract String getSessionId() / abstract void setSessionId(String s)}
     * produces a backing field {@code sessionId} plus concrete
     * getter/setter bodies that read/write through {@code _cmpRead} /
     * {@code _cmpWrite} (which themselves go through
     * {@link CmpFieldStoreLocator}).
     */
    private void generateCmpAccessors(CtClass concrete, CtClass abstractSbb, OffHeap offHeap)
            throws javassist.CannotCompileException, javassist.NotFoundException {
        Set<String> fieldsCreated = new HashSet<String>();
        for (CtMethod method : abstractSbb.getDeclaredMethods()) {
            if (!Modifier.isAbstract(method.getModifiers())) {
                continue;
            }
            String mName = method.getName();
            boolean isGetter = mName.startsWith("get") && method.getParameterTypes().length == 0;
            boolean isSetter = mName.startsWith("set") && method.getParameterTypes().length == 1;
            if (!isGetter && !isSetter) {
                continue;
            }
            String fieldName = toCmpFieldName(mName);
            CtClass fieldType = isGetter
                    ? method.getReturnType()
                    : method.getParameterTypes()[0];

            CtMethod getter = null;
            CtMethod setter = null;
            try {
                getter = isGetter ? method : findCompanion(abstractSbb, "get" + mName.substring(3));
                setter = isSetter ? method : findCompanion(abstractSbb, "set" + mName.substring(3));
            } catch (javassist.NotFoundException ignored) {
                // companion method not declared; treat as no-op
            }
            if (getter == null && setter == null) {
                continue;
            }
            if (!fieldsCreated.add(fieldName)) {
                continue; // already handled in a previous iteration
            }
            // For the case where only a setter (no getter) was declared, fall
            // back to declaring a no-arg getter that returns the field type.
            if (getter == null) {
                getter = synthesizeNoArgGetter(abstractSbb, fieldName, fieldType);
            }
            if (offHeap != null && isOffHeapSupported(fieldType.getName())) {
                generateOffHeapCmpField(fieldName, fieldType, concrete, getter, setter,
                        offHeap.fallback());
            } else {
                generateCmpField(fieldName, fieldType, concrete, getter, setter);
            }
        }
    }

    /**
     * Build a synthetic {@code getXxx} signature that matches the setter's
     * parameter type. Used when an SBB declares only a setter (rare).
     */
    private CtMethod synthesizeNoArgGetter(CtClass abstractSbb, String fieldName, CtClass returnType) throws javassist.NotFoundException {
        // Create a CtMethod descriptor so generateCmpField can copy it.
        CtMethod synth = new CtMethod(returnType,
                "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1),
                new CtClass[0], abstractSbb);
        synth.setModifiers(javassist.Modifier.ABSTRACT | javassist.Modifier.PUBLIC);
        return synth;
    }

    private static CtMethod findCompanion(CtClass abstractSbb, String name) throws javassist.NotFoundException {
        try {
            return abstractSbb.getDeclaredMethod(name);
        } catch (javassist.NotFoundException e) {
            return null;
        }
    }

    /**
     * Generate empty {@code sbbLoad} / {@code sbbStore} if abstract. State
     * snapshot is handled by {@code DistributedSbbEntityPool} — these are
     * stubs only because the abstract class declares them.
     */
    private void generateSbbLoadStore(CtClass concrete, CtClass abstractSbb)
            throws javassist.CannotCompileException {
        for (CtMethod method : abstractSbb.getDeclaredMethods()) {
            if (!Modifier.isAbstract(method.getModifiers())) {
                continue;
            }
            String name = method.getName();
            if (!"sbbLoad".equals(name) && !"sbbStore".equals(name)) {
                continue;
            }
            CtMethod impl = CtNewMethod.copy(method, concrete, null);
            impl.setModifiers(impl.getModifiers() & ~Modifier.ABSTRACT);
            impl.setBody("{ /* no-op — state handled by SbbEntityPool */ }");
            concrete.addMethod(impl);
        }
    }

    // ------------------------------------------------------------------------
    //  Off-heap emission (design doc §5 — Option A, helper-call form)
    // ------------------------------------------------------------------------

    /** Field types storable off-heap; anything else falls back to the heap path. */
    static boolean isOffHeapSupported(String typeName) {
        switch (typeName) {
            case "int":
            case "long":
            case "boolean":
            case "double":
            case "java.lang.String":
            case "byte[]":
                return true;
            default:
                return false;
        }
    }

    /**
     * Emit the {@code OffHeapBindable} contract + lazy layout holder:
     * a volatile slot base, a cached {@code OffHeapLayout}, bind/unbind.
     */
    private void emitOffHeapHelpers(CtClass concrete, String abstractClassName)
            throws javassist.CannotCompileException, javassist.NotFoundException {
        concrete.addInterface(concrete.getClassPool()
                .get("com.microjainslee.api.OffHeapBindable"));

        concrete.addField(CtField.make(
                "private volatile long _offHeapBase;", concrete));
        concrete.addField(CtField.make(
                "private static volatile com.microjainslee.core.offheap.OffHeapLayout _OHL;",
                concrete));

        concrete.addMethod(CtNewMethod.make(
                "private static com.microjainslee.core.offheap.OffHeapLayout _ohl() {"
                + " com.microjainslee.core.offheap.OffHeapLayout l = _OHL;"
                + " if (l == null) {"
                + "   l = com.microjainslee.core.offheap.OffHeapRuntime.layoutFor("
                +       abstractClassName + ".class);"
                + "   _OHL = l;"
                + " }"
                + " return l; }", concrete));
        concrete.addMethod(CtNewMethod.make(
                "public void bindSlot(long addr) { this._offHeapBase = addr; }", concrete));
        concrete.addMethod(CtNewMethod.make(
                "public void unbindSlot() { this._offHeapBase = 0L; }", concrete));
        concrete.addMethod(CtNewMethod.make(
                "public long slotAddress() { return this._offHeapBase; }", concrete));
        concrete.addMethod(CtNewMethod.make(
                "public void setEntityId(String id) { this._sbbEntityId = id; }", concrete));
    }

    /**
     * Emit off-heap getter/setter for one CMP field. The field index into
     * the layout is resolved once (per class, per field) and cached in a
     * static — after warm-up an access is: 2 volatile reads + 1 static
     * helper call, zero Map lookups, zero boxing.
     */
    private void generateOffHeapCmpField(String fieldName, CtClass type, CtClass concrete,
                                         CtMethod getter, CtMethod setter, boolean fallback)
            throws javassist.CannotCompileException {
        String idxField = "_ohIdx_" + fieldName;
        concrete.addField(CtField.make(
                "private static volatile int " + idxField + " = -1;", concrete));

        String typeName = type.getName();
        String acc = "com.microjainslee.core.offheap.OffHeapCmpAccessor";
        String readCall;
        String writeCall;
        switch (typeName) {
            case "int":              readCall = acc + ".readInt(l, b, i)";     writeCall = acc + ".writeInt(l, b, i, $1)";     break;
            case "long":             readCall = acc + ".readLong(l, b, i)";    writeCall = acc + ".writeLong(l, b, i, $1)";    break;
            case "boolean":          readCall = acc + ".readBoolean(l, b, i)"; writeCall = acc + ".writeBoolean(l, b, i, $1)"; break;
            case "double":           readCall = acc + ".readDouble(l, b, i)";  writeCall = acc + ".writeDouble(l, b, i, $1)";  break;
            case "java.lang.String": readCall = acc + ".readString(l, b, i)";  writeCall = acc + ".writeString(l, b, i, $1)";  break;
            case "byte[]":           readCall = acc + ".readBytes(l, b, i)";   writeCall = acc + ".writeBytes(l, b, i, $1)";   break;
            default: throw new javassist.CannotCompileException(
                    "Off-heap unsupported type " + typeName + " for field " + fieldName);
        }

        String prologue =
                " com.microjainslee.core.offheap.OffHeapLayout l = _ohl();"
                + " long b = this._offHeapBase;"
                + " int i = " + idxField + ";"
                + " if (i < 0) { i = l.indexOf(\"" + fieldName + "\"); " + idxField + " = i; }";

        // Fallback mode: when no slot is bound, run the original heap body
        // (tests / mixed deployments — design doc §5 dual-accessor).
        String getterFallback = fallback
                ? " if (b == 0L) " + buildGetterBody(fieldName, type) : "";
        String setterFallback = fallback
                ? " if (this._offHeapBase == 0L) " + buildSetterBody(fieldName, type)
                        .replace("; }", "; return; }") : "";

        CtMethod getterImpl = CtNewMethod.copy(getter, concrete, null);
        getterImpl.setModifiers(getterImpl.getModifiers() & ~Modifier.ABSTRACT);
        getterImpl.setBody("{" + prologue + getterFallback
                + " return " + readCall + "; }");
        concrete.addMethod(getterImpl);

        if (setter != null) {
            CtMethod setterImpl = CtNewMethod.copy(setter, concrete, null);
            setterImpl.setModifiers(setterImpl.getModifiers() & ~Modifier.ABSTRACT);
            setterImpl.setBody("{" + prologue + setterFallback
                    + " " + writeCall + "; }");
            concrete.addMethod(setterImpl);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Emitted OFF-HEAP CMP field {} {}", typeName, fieldName);
        }
    }

    // ------------------------------------------------------------------------
    //  Body emission helpers — kept package-private so tests can introspect.
    // ------------------------------------------------------------------------

    /**
     * Build the getter body. The returned string is a Javassist snippet that
     * delegates to the synthetic {@code _cmpRead} helper. Uses an explicit
     * if/return block instead of a ternary because Javassist's JDT
     * frontend rejects {@code ?:} when the two arms have different
     * reference-vs-primitive types (e.g. {@code int} literal vs.
     * {@link Integer} un-box call).
     */
    static String buildGetterBody(String fieldName, CtClass type) {
        String typeName = type.getName();
        String readCall = "{ Object _v = _cmpRead(\"" + fieldName + "\");\n";
        if (isPrimitive(typeName)) {
            String boxed = boxedOf(typeName);
            String defaultLit = defaultLiteral(typeName);
            String unbox = unboxMethod(typeName);
            return readCall
                    + "  if (_v == null) return " + defaultLit + ";\n"
                    + "  return ((" + boxed + ") _v)." + unbox + "(); }";
        }
        return readCall
                + "  if (_v == null) return null;\n"
                + "  return (" + typeName + ") _v; }";
    }

    /**
     * Build the setter body. Writes through {@code _cmpWrite} so every
     * setter goes through the same persistence path.
     */
    static String buildSetterBody(String fieldName, CtClass type) {
        String typeName = type.getName();
        String writeCall = "{ _cmpWrite(\"" + fieldName + "\", ";
        if (isPrimitive(typeName)) {
            String boxed = boxedOf(typeName);
            return writeCall + boxed + ".valueOf($1)); }";
        }
        return writeCall + "(Object) $1); }";
    }

    private static boolean isPrimitive(String typeName) {
        switch (typeName) {
            case "int":
            case "long":
            case "boolean":
            case "double":
            case "float":
            case "short":
            case "byte":
            case "char":
            case "void":
                return true;
            default:
                return false;
        }
    }

    /**
     * Map a primitive type name to its boxed wrapper type.
     */
    static String boxedOf(String primitiveName) {
        switch (primitiveName) {
            case "int":     return "java.lang.Integer";
            case "long":    return "java.lang.Long";
            case "boolean": return "java.lang.Boolean";
            case "double":  return "java.lang.Double";
            case "float":   return "java.lang.Float";
            case "short":   return "java.lang.Short";
            case "byte":    return "java.lang.Byte";
            case "char":    return "java.lang.Character";
            default:        throw new IllegalArgumentException("Not a primitive: " + primitiveName);
        }
    }

    /**
     * Map a primitive type name to its default value literal.
     */
    static String defaultLiteral(String primitiveName) {
        switch (primitiveName) {
            case "int":
            case "short":
            case "byte":
            case "char":     return "0";
            case "long":    return "0L";
            case "boolean": return "false";
            case "double":  return "0.0d";
            case "float":   return "0.0f";
            default:        throw new IllegalArgumentException("Not a primitive: " + primitiveName);
        }
    }

    /**
     * Map a primitive type name to the un-box accessor method name on its
     * wrapper (e.g. {@code intValue}, {@code longValue}, {@code booleanValue}).
     */
    static String unboxMethod(String primitiveName) {
        return primitiveName + "Value";
    }

    /**
     * Map a boxed wrapper type name to its primitive descriptor cast. Kept
     * for backward compatibility with the public API surface used by
     * deployments that emit their own Javassist snippets; not used by
     * the getter/setter bodies above (which use direct casts instead).
     */
    static String boxedCast(String typeName) {
        return "(" + typeName + ")";
    }

    // ------------------------------------------------------------------------
    //  Solution A — load .class from the deploy dir via URLClassLoader.
    // ------------------------------------------------------------------------

    private Class<?> loadFromDeployDir(String className, Path deployDir, ClassLoader parent)
            throws IOException {
        URLClassLoader loader = new URLClassLoader(
                new java.net.URL[]{ deployDir.toUri().toURL() },
                parent == null ? ConcreteSbbGenerator.class.getClassLoader() : parent);
        try {
            return loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new IOException("Generated .class not found in deployDir: " + className, e);
        }
    }
}
