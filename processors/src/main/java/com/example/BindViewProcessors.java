package com.example;

import com.example.entity.VariMsg;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

@AutoService(Processor.class)
public class BindViewProcessors extends AbstractProcessor {
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }


    Map<String, VariMsg> veMap = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        veMap.clear();
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindView.class);
        //一、收集信息
        for (Element element : elements) {
            /*检查类型*/
            if (!(element instanceof VariableElement)) {
                return false;
            }
            VariableElement variableElement = (VariableElement) element;
            /*方法名*/
//            String name = variableElement.getSimpleName().toString();
            /*方法对象类*/
//            String type = variableElement.asType().toString();

            /*获取类信息*/
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            /*类的绝对路径*/
            String qualifiedName = typeElement.getQualifiedName().toString();
            /*类名*/
            String clsName = typeElement.getSimpleName().toString();
            /*获取包名*/
            String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();

            BindView annotation = variableElement.getAnnotation(BindView.class);
            int id = annotation.value();

            VariMsg variMsg = veMap.get(qualifiedName);
            if (variMsg == null) {
                variMsg = new VariMsg(packageName, clsName);
                variMsg.getVarMap().put(id, variableElement);
                veMap.put(qualifiedName, variMsg);
            } else {
                variMsg.getVarMap().put(id, variableElement);
            }

//
//        ClassName InterfaceName = ClassName.bestGuess("com.example.annotation.api.ViewInjector");
//            ClassName host = ClassName.bestGuess(qualifiedName);
//
//            MethodSpec main = MethodSpec.methodBuilder("inject")
//                    .addModifiers(Modifier.PUBLIC)
//                    .returns(void.class)
//                    .addAnnotation(Override.class)
//                    .addParameter(host, "host")
//                    .addParameter(Object.class, "object")
//                    .addCode(""
//                            + " if(object instanceof android.app.Activity){\n"
//                            + " host." + name + " = (" + type + ")(((android.app.Activity)object).findViewById(" + id + "));\n"
//                            + " }\n"
//                            + "else{\n"
//                            + " host." + name + " = (" + type + ")(((android.view.View)object).findViewById(" + id + "));\n"
//                            + "}\n")
//                    .build();
//
//            TypeSpec helloWorld = TypeSpec.classBuilder(qName + "$$ViewInjector")
//                    .addModifiers(Modifier.PUBLIC)
//                    .addMethod(main)
//                    .addSuperinterface(ParameterizedTypeName.get(InterfaceName, host))
//                    .build();
//
//            try {
//                // 生成 com.example.HelloWorld.java
//                JavaFile javaFile = JavaFile.builder(packageName, helloWorld)
//                        .addFileComment(" This codes are generated automatically. Do not modify!")
//                        .build();
//                //　生成文件
//                javaFile.writeTo(filer);
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println("e--->" + e.getMessage());
//            }


        }

        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        for (String key : veMap.keySet()) {
            ClassName InterfaceName = ClassName.bestGuess("com.example.ViewInjector");
            ClassName host = ClassName.bestGuess(key);
            VariMsg variMsg = veMap.get(key);

            StringBuilder builder = new StringBuilder();
            builder.append(" if(object instanceof android.app.Activity){\n");
            builder.append(code(variMsg.getVarMap(), "android.app.Activity"));
            builder.append("}\n");
            builder.append("else{\n");
            builder.append(code(variMsg.getVarMap(), "android.view.View"));
            builder.append("}\n");

            MethodSpec main = MethodSpec.methodBuilder("inject")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addAnnotation(Override.class)
                    .addParameter(host, "host")
                    .addParameter(Object.class, "object")
                    .addCode(builder.toString())
                    .build();

            TypeSpec helloWorld = TypeSpec.classBuilder(variMsg.getClsName() + "ViewInjector")
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(main)
                    .addSuperinterface(ParameterizedTypeName.get(InterfaceName, host))
                    .build();

            try {
                JavaFile javaFile = JavaFile.builder(variMsg.getPk(), helloWorld)
                        .addFileComment(" This codes are generated automatically. Do not modify!")
                        .build();
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("e--->" + e.getMessage());
            }
        }
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        return true;
    }


    /**
     * 根据注解对象生成code方法体
     *
     * @param map
     * @param pk
     * @return
     */
    private String code(Map<Integer, VariableElement> map, String pk) {
        StringBuilder builder = new StringBuilder();
        for (Integer id : map.keySet()) {
            VariableElement variableElement = map.get(id);
            String name = variableElement.getSimpleName().toString();
            String type = variableElement.asType().toString();

            builder.append("host." + name + " = (" + type + ")(((" + pk + ")object).findViewById(" + id + "));\n");
        }
        return builder.toString();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<>();
        annotataions.add(BindView.class.getCanonicalName());
        return annotataions;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
