package com.foobar.showme.apt;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Generuje klasę {@code <Name>Description} z metodą {@code typeOf(field)} zwracającą
 * typ danego pola opisywanej klasy.
 *
 * OBJAW (jak w prezentacji): wygenerowany kod zawsze twierdzi, że typ pola to "String",
 * mimo że pole jest np. int-em. Znajdź to debugując PROCESOR przez `mvnDebug`.
 */
@SupportedAnnotationTypes("com.foobar.showme.apt.Describe")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class DescribeProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element annotated : roundEnv.getElementsAnnotatedWith(Describe.class)) {
            if (annotated.getKind() == ElementKind.CLASS) {
                generate((TypeElement) annotated);
            }
        }
        return true;
    }

    private void generate(TypeElement type) {
        String pkg = processingEnv.getElementUtils().getPackageOf(type).getQualifiedName().toString();
        String simple = type.getSimpleName().toString();
        String genName = simple + "Description";

        Map<String, String> fieldTypes = new LinkedHashMap<>();
        for (Element member : type.getEnclosedElements()) {
            if (member.getKind() == ElementKind.FIELD) {
                String name = member.getSimpleName().toString();
                fieldTypes.put(name, mapType((VariableElement) member));
            }
        }

        try {
            String fqcn = pkg.isEmpty() ? genName : pkg + "." + genName;
            JavaFileObject file = processingEnv.getFiler().createSourceFile(fqcn, type);
            try (Writer w = file.openWriter()) {
                if (!pkg.isEmpty()) {
                    w.write("package " + pkg + ";\n\n");
                }
                w.write("public final class " + genName + " {\n");
                w.write("    public static String typeOf(String field) {\n");
                w.write("        switch (field) {\n");
                for (Map.Entry<String, String> e : fieldTypes.entrySet()) {
                    w.write("            case \"" + e.getKey() + "\": return \"" + e.getValue() + "\";\n");
                }
                w.write("            default: return \"unknown\";\n");
                w.write("        }\n");
                w.write("    }\n");
                w.write("}\n");
            }
        } catch (Exception e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString(), type);
        }
    }

    private String mapType(VariableElement field) {
        return field.asType().toString();
    }
}
