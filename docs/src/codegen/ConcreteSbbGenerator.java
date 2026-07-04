package com.microjainslee.codegen;

import javassist.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Deploy-time concrete SBB class generator dùng Javassist (Solution A).
 *
 * Sinh ra concrete class cho mỗi abstract SBB class với:
 *   - CMP backing fields (private, typed, direct field access)
 *   - CMP getter/setter implementations (không dùng reflection trên hot path)
 *   - sbbLoad/sbbStore stubs cho state snapshot
 *
 * Solution A từ optimizejainsleep2.md:
 *   - Ghi .class vào deployDir (persist)
 *   - Load từ deployment ClassLoader
 *   - Không gọi toClass() in-process → tránh PermGen/Metaspace leak
 *   - Không generate trên hot path (Disruptor event delivery) — chỉ tại deploy time
 *
 * Module: jainslee-codegen (NEW)
 * Dependency: org.javassist:javassist:3.30.0-GA
 */
public class ConcreteSbbGenerator {

    private static final Logger LOG = LogManager.getLogger(ConcreteSbbGenerator.class);
    static final String CONCRETE_SUFFIX = "$Concrete";

    // Cache: abstract class → generated concrete Class (tránh regenerate)
    private final Map<String, Class<?>> generatedCache = new ConcurrentHashMap<>();

    /**
     * Generate (hoặc return cached) concrete implementation của abstract SBB class.
     *
     * @param sbbAbstractClass abstract SBB class có CMP abstract methods
     * @param deployDir        thư mục ghi .class file
     * @return concrete class đã load vào ClassLoader
     */
    public Class<?> getOrGenerate(Class<?> sbbAbstractClass, Path deployDir) throws Exception {
        return generatedCache.computeIfAbsent(
                sbbAbstractClass.getName(),
                name -> {
                    try {
                        return doGenerate(sbbAbstractClass, deployDir);
                    } catch (Exception e) {
                        throw new RuntimeException("Codegen failed for " + name, e);
                    }
                }
        );
    }

    private Class<?> doGenerate(Class<?> sbbAbstractClass, Path deployDir) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        pool.appendClassPath(new LoaderClassPath(sbbAbstractClass.getClassLoader()));

        String concreteName = sbbAbstractClass.getName() + CONCRETE_SUFFIX;
        CtClass abstractSbb = pool.get(sbbAbstractClass.getName());
        CtClass concreteSbb = pool.makeClass(concreteName, abstractSbb);

        // 1. Generate CMP field + accessor per abstract get/set pair
        generateCmpAccessors(concreteSbb, abstractSbb, pool);

        // 2. Generate no-arg constructor (Javassist default is fine, explicit for clarity)
        CtConstructor ctor = CtNewConstructor.defaultConstructor(concreteSbb);
        concreteSbb.addConstructor(ctor);

        // 3. Generate sbbLoad/sbbStore stubs (nếu chưa có concrete impl)
        generateSbbLoadStore(concreteSbb, abstractSbb);

        // Write .class to deployDir — Solution A
        Files.createDirectories(deployDir);
        concreteSbb.writeFile(deployDir.toString());
        LOG.infof("Generated: %s → %s/%s.class",
                sbbAbstractClass.getSimpleName(), deployDir, concreteName.replace('.', '/'));

        // Load from deployDir via deployment ClassLoader
        return loadFromDeployDir(concreteName, deployDir, sbbAbstractClass.getClassLoader());
    }

    /**
     * Generate backing field + getter + setter cho mỗi abstract CMP pair.
     *
     * Pattern: abstract String getSessionId() / abstract void setSessionId(String s)
     *   → private String sessionId; (backing field)
     *   → String getSessionId() { return this.sessionId; }
     *   → void setSessionId(String s) { this.sessionId = s; }
     */
    private void generateCmpAccessors(CtClass concrete, CtClass abstractSbb,
                                      ClassPool pool) throws Exception {
        // Track which fields already created (avoid duplicates for get+set pairs)
        java.util.Set<String> fieldsCreated = new java.util.HashSet<>();

        for (CtMethod method : abstractSbb.getDeclaredMethods()) {
            if (!Modifier.isAbstract(method.getModifiers())) continue;

            String mName = method.getName();
            boolean isGetter = mName.startsWith("get") && method.getParameterTypes().length == 0;
            boolean isSetter = mName.startsWith("set") && method.getParameterTypes().length == 1;
            if (!isGetter && !isSetter) continue;

            String fieldName = toCmpFieldName(mName);
            CtClass fieldType = isGetter
                    ? method.getReturnType()
                    : method.getParameterTypes()[0];

            // Add backing field once per pair
            if (!fieldsCreated.contains(fieldName)) {
                CtField field = new CtField(fieldType, fieldName, concrete);
                field.setModifiers(Modifier.PRIVATE);
                concrete.addField(field);
                fieldsCreated.add(fieldName);
                LOG.tracef("CMP field: %s %s", fieldType.getSimpleName(), fieldName);
            }

            // Implement the abstract method
            CtMethod impl = CtNewMethod.copy(method, concrete, null);
            // Remove abstract modifier
            impl.setModifiers(impl.getModifiers() & ~Modifier.ABSTRACT);

            if (isGetter) {
                impl.setBody("{ return this." + fieldName + "; }");
            } else {
                impl.setBody("{ this." + fieldName + " = $1; }");
            }
            concrete.addMethod(impl);
        }
    }

    /**
     * Generate empty sbbLoad / sbbStore if abstract (some SBBs declare them abstract).
     * State snapshot is handled by DistributedSbbEntityPool — these are stubs.
     */
    private void generateSbbLoadStore(CtClass concrete, CtClass abstractSbb) throws Exception {
        for (CtMethod method : abstractSbb.getDeclaredMethods()) {
            if (!Modifier.isAbstract(method.getModifiers())) continue;
            String name = method.getName();
            if (!name.equals("sbbLoad") && !name.equals("sbbStore")) continue;

            CtMethod impl = CtNewMethod.copy(method, concrete, null);
            impl.setModifiers(impl.getModifiers() & ~Modifier.ABSTRACT);
            impl.setBody("{ /* no-op — state handled by SbbEntityPool */ }");
            concrete.addMethod(impl);
        }
    }

    /**
     * Derive backing field name từ CMP method name.
     * getCmpFieldSessionId → sessionId
     * getSessionId → sessionId
     * set* → same derivation
     */
    static String toCmpFieldName(String methodName) {
        String stripped = methodName.substring(3); // remove get/set prefix
        if (stripped.startsWith("CmpField")) {
            stripped = stripped.substring(8);
        }
        if (stripped.isEmpty()) return "_unnamed";
        return Character.toLowerCase(stripped.charAt(0)) + stripped.substring(1);
    }

    private Class<?> loadFromDeployDir(String className, Path deployDir,
                                       ClassLoader parent) throws Exception {
        java.net.URLClassLoader loader = new java.net.URLClassLoader(
                new java.net.URL[]{deployDir.toUri().toURL()}, parent);
        return loader.loadClass(className);
    }
}
