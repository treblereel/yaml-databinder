package org.treblereel.gwt.yaml.deserializer;

import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import org.treblereel.gwt.yaml.TypeUtils;
import org.treblereel.gwt.yaml.api.JacksonContextProvider;
import org.treblereel.gwt.yaml.api.YAMLDeserializationContext;
import org.treblereel.gwt.yaml.api.YAMLDeserializer;
import org.treblereel.gwt.yaml.api.YAMLDeserializerParameters;
import org.treblereel.gwt.yaml.api.deser.bean.AbstractBeanYAMLDeserializer;
import org.treblereel.gwt.yaml.api.deser.bean.BeanPropertyDeserializer;
import org.treblereel.gwt.yaml.api.deser.bean.HasDeserializerAndParameters;
import org.treblereel.gwt.yaml.api.deser.bean.Instance;
import org.treblereel.gwt.yaml.api.deser.bean.InstanceBuilder;
import org.treblereel.gwt.yaml.api.deser.bean.MapLike;
import org.treblereel.gwt.yaml.api.stream.YAMLReader;
import org.treblereel.gwt.yaml.context.GenerationContext;
import org.treblereel.gwt.yaml.definition.BeanDefinition;
import org.treblereel.gwt.yaml.definition.PropertyDefinition;
import org.treblereel.gwt.yaml.generator.AbstractGenerator;
import org.treblereel.gwt.yaml.logger.TreeLogger;

/**
 * @author Dmitrii Tikhomirov
 * Created by treblereel 3/18/20
 */
public class DeserializerGenerator extends AbstractGenerator {

    public DeserializerGenerator(GenerationContext context, TreeLogger logger) {
        super(context, logger.branch(TreeLogger.INFO, "Deserializers generation started"));
    }

    @Override
    protected String getMapperName(TypeElement type) {
        return context.getTypeUtils().deserializerName(type.asType());
    }

    @Override
    protected void configureClassType(BeanDefinition type) {
        cu.addImport(JacksonContextProvider.class);
        cu.addImport(YAMLDeserializationContext.class);
        cu.addImport(YAMLDeserializer.class);
        cu.addImport(YAMLDeserializerParameters.class);
        cu.addImport(AbstractBeanYAMLDeserializer.class);
        cu.addImport(BeanPropertyDeserializer.class);
        cu.addImport(HasDeserializerAndParameters.class);
        cu.addImport(Instance.class);
        cu.addImport(Map.class);
        cu.addImport(MapLike.class);
        cu.addImport(InstanceBuilder.class);
        cu.addImport(YAMLReader.class);
        cu.addImport(type.getQualifiedName());

        declaration.getExtendedTypes().add(new ClassOrInterfaceType()
                                                   .setName(AbstractBeanYAMLDeserializer.class.getSimpleName())
                                                   .setTypeArguments(new ClassOrInterfaceType()
                                                                             .setName(type.getSimpleName())
                                                   ));
    }

    @Override
    protected void getType(BeanDefinition type) {
        declaration.addMethod("getDeserializedType", Modifier.Keyword.PUBLIC)
                .addAnnotation(Override.class)
                .setType(Class.class)
                .getBody().ifPresent(body -> body.addStatement(new ReturnStmt(
                new FieldAccessExpr(
                        new NameExpr(type.getSimpleName()), "class"))));

        declaration.addMethod("getRootElement", Modifier.Keyword.PROTECTED)
                .addAnnotation(Override.class)
                .setType(String.class)
                .getBody().ifPresent(body -> body.addStatement(new ReturnStmt(
                new StringLiteralExpr(type.getRootElement()))));
    }

    @Override
    protected void init(BeanDefinition beanDefinition) {
        logger.log(TreeLogger.INFO, "Generating " + context.getTypeUtils().deserializerName(beanDefinition.getBean()));
        initDeserializers(beanDefinition);
        initInstanceBuilder(beanDefinition);
    }

    private void initDeserializers(BeanDefinition beanDefinition) {
        MethodDeclaration initSerializers = declaration.addMethod("initDeserializers", Modifier.Keyword.PROTECTED);

        initSerializers.addAnnotation(Override.class)
                .setType(new ClassOrInterfaceType()
                                 .setName(MapLike.class.getSimpleName())
                                 .setTypeArguments(new ClassOrInterfaceType()
                                                           .setName(BeanPropertyDeserializer.class.getSimpleName())
                                                           .setTypeArguments(new ClassOrInterfaceType()
                                                                                     .setName(beanDefinition.getElement().getSimpleName().toString()),
                                                                             new ClassOrInterfaceType()
                                                                                     .setName("?"))
                                 )
                );
        ClassOrInterfaceType varType = new ClassOrInterfaceType().setName("MapLike")
                .setTypeArguments(new ClassOrInterfaceType().setName("BeanPropertyDeserializer")
                                          .setTypeArguments(new ClassOrInterfaceType().setName(beanDefinition.getElement().getSimpleName().toString()),
                                                            new ClassOrInterfaceType().setName("?")));

        VariableDeclarator map = new VariableDeclarator();
        map.setType(varType);
        map.setName("map");
        map.setInitializer(new NameExpr("JacksonContextProvider.get().mapLikeFactory().make()"));

        ExpressionStmt expressionStmt = new ExpressionStmt();
        VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr();
        variableDeclarationExpr.setModifiers(Modifier.Keyword.FINAL);
        expressionStmt.setExpression(variableDeclarationExpr);
        variableDeclarationExpr.getVariables().add(map);

        initSerializers.getBody().ifPresent(body -> {
            body.addStatement(expressionStmt);
            beanDefinition.getFields().forEach(field -> addBeanPropertyDeserializer(body, beanDefinition.getElement(), field));
            body.addStatement(new ReturnStmt("map"));
        });
    }

    private void initInstanceBuilder(BeanDefinition type) {
        MethodDeclaration initInstanceBuilder = declaration.addMethod("initInstanceBuilder", Modifier.Keyword.PROTECTED);
        initInstanceBuilder.addAnnotation(Override.class)
                .setType(new ClassOrInterfaceType()
                                 .setName(InstanceBuilder.class.getSimpleName())
                                 .setTypeArguments(new ClassOrInterfaceType()
                                                           .setName(type.getSimpleName())));
        VariableDeclarator deserializers = new VariableDeclarator();
        deserializers.setType("MapLike<HasDeserializerAndParameters>");
        deserializers.setName("deserializers");
        deserializers.setInitializer("null");

        ExpressionStmt expressionStmt = new ExpressionStmt();
        VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr();
        variableDeclarationExpr.setModifiers(Modifier.Keyword.FINAL);
        expressionStmt.setExpression(variableDeclarationExpr);
        variableDeclarationExpr.getVariables().add(deserializers);

        initInstanceBuilder.getBody().ifPresent(body -> {
            body.addStatement(variableDeclarationExpr);
            addInstanceBuilder(type, body);
        });
    }

    private void addBeanPropertyDeserializer(BlockStmt body, TypeElement type, PropertyDefinition field) {
        NodeList<BodyDeclaration<?>> anonymousClassBody = new NodeList<>();

        ClassOrInterfaceType typeArg = getWrappedType(field.getProperty());
        ClassOrInterfaceType beanPropertyDeserializer = new ClassOrInterfaceType()
                .setName(BeanPropertyDeserializer.class.getSimpleName());
        beanPropertyDeserializer.setTypeArguments(
                new ClassOrInterfaceType().setName(type.getSimpleName().toString()),
                typeArg);

        body.addStatement(new MethodCallExpr(new NameExpr("map"), "put")
                                  .addArgument(new StringLiteralExpr(field.getPropertyName()))
                                  .addArgument(new ObjectCreationExpr()
                                                       .setType(beanPropertyDeserializer)
                                                       .setAnonymousClassBody(anonymousClassBody)
                                  ));
        addNewDeserializer(field, anonymousClassBody);
        setValue(type, typeArg, field, anonymousClassBody);
    }

    private void addInstanceBuilder(BeanDefinition type, BlockStmt body) {
        ObjectCreationExpr instanceBuilder = new ObjectCreationExpr();
        ClassOrInterfaceType instanceBuilderType = new ClassOrInterfaceType()
                .setName(InstanceBuilder.class.getSimpleName())
                .setTypeArguments(new ClassOrInterfaceType()
                                          .setName(type.getSimpleName()));

        instanceBuilder.setType(instanceBuilderType);
        NodeList<BodyDeclaration<?>> anonymousClassBody = new NodeList<>();
        instanceBuilder.setAnonymousClassBody(anonymousClassBody);

        newInstance(type, anonymousClassBody);
        getParametersDeserializer(anonymousClassBody);
        create(type, anonymousClassBody);

        body.addStatement(new ReturnStmt(instanceBuilder));
    }

    private ClassOrInterfaceType getWrappedType(VariableElement field) {
        ClassOrInterfaceType typeArg = new ClassOrInterfaceType().setName(TypeUtils.wrapperType(field.asType()));
        if (field.asType() instanceof DeclaredType) {
            if (!((DeclaredType) field.asType()).getTypeArguments().isEmpty()) {
                NodeList<Type> types = new NodeList<>();
                ((DeclaredType) field.asType()).getTypeArguments()
                        .forEach(t -> types.add(new ClassOrInterfaceType().setName(TypeUtils.wrapperType(t))));
                typeArg.setTypeArguments(types);
            }
        }
        return typeArg;
    }

    private void addNewDeserializer(PropertyDefinition field, NodeList<BodyDeclaration<?>> anonymousClassBody) {
        MethodDeclaration method = new MethodDeclaration();
        method.setModifiers(Modifier.Keyword.PROTECTED);
        method.addAnnotation(Override.class);
        method.setName("newDeserializer");
        method.setType(new ClassOrInterfaceType().setName("YAMLDeserializer<?>"));

        method.getBody().ifPresent(body -> body.addAndGetStatement(
                new ReturnStmt().setExpression(field.getFieldDeserializer(cu))));
        anonymousClassBody.add(method);
    }

    private void setValue(TypeElement type, ClassOrInterfaceType fieldType, PropertyDefinition field, NodeList<BodyDeclaration<?>> anonymousClassBody) {
        MethodDeclaration method = new MethodDeclaration();
        method.setModifiers(Modifier.Keyword.PUBLIC);
        method.addAnnotation(Override.class);
        method.setName("setValue");
        method.setType("void");
        method.addParameter(type.getSimpleName().toString(), "bean");
        method.addParameter(fieldType, "value");
        method.addParameter(YAMLDeserializationContext.class.getSimpleName(), "ctx");

        method.getBody().ifPresent(body -> body.addAndGetStatement(getFieldAccessor(field)));
        anonymousClassBody.add(method);
    }

    private void newInstance(BeanDefinition type, NodeList<BodyDeclaration<?>> anonymousClassBody) {
        MethodDeclaration method = new MethodDeclaration();
        method.setModifiers(Modifier.Keyword.PUBLIC);
        method.addAnnotation(Override.class);
        method.setName("newInstance");
        method.setType(new ClassOrInterfaceType().setName("Instance")
                               .setTypeArguments(new ClassOrInterfaceType().setName(type.getSimpleName())));
        addParameter(method, "YAMLReader", "reader");
        addParameter(method, "YAMLDeserializationContext", "ctx");
        addParameter(method, "YAMLDeserializerParameters", "params");
        addParameter(method, "Map<String, String>", "bufferedProperties");
        addParameter(method, "Map<String, Object>", "bufferedPropertiesValues");

        ObjectCreationExpr instanceBuilder = new ObjectCreationExpr();
        ClassOrInterfaceType instanceBuilderType = new ClassOrInterfaceType()
                .setName(Instance.class.getSimpleName())
                .setTypeArguments(new ClassOrInterfaceType()
                                          .setName(type.getSimpleName()));

        instanceBuilder.setType(instanceBuilderType);
        instanceBuilder.addArgument(new MethodCallExpr("create"));
        instanceBuilder.addArgument("bufferedProperties");

        method.getBody().ifPresent(body -> body.addAndGetStatement(new ReturnStmt().setExpression(instanceBuilder)));
        anonymousClassBody.add(method);
    }

    private void getParametersDeserializer(NodeList<BodyDeclaration<?>> anonymousClassBody) {
        MethodDeclaration method = new MethodDeclaration();
        method.setModifiers(Modifier.Keyword.PUBLIC);
        method.addAnnotation(Override.class);
        method.setName("getParametersDeserializer");
        method.setType(new ClassOrInterfaceType().setName("MapLike")
                               .setTypeArguments(new ClassOrInterfaceType()
                                                         .setName("HasDeserializerAndParameters")));
        method.getBody().ifPresent(body -> body.addAndGetStatement(new ReturnStmt().setExpression(new NameExpr("deserializers"))));
        anonymousClassBody.add(method);
    }

    private void create(BeanDefinition type, NodeList<BodyDeclaration<?>> anonymousClassBody) {
        MethodDeclaration method = new MethodDeclaration();
        method.setModifiers(Modifier.Keyword.PRIVATE);
        method.setName("create");
        method.setType(new ClassOrInterfaceType().setName(type.getSimpleName()));

        ObjectCreationExpr instanceBuilder = new ObjectCreationExpr();
        ClassOrInterfaceType instanceBuilderType = new ClassOrInterfaceType()
                .setName(type.getSimpleName());
        instanceBuilder.setType(instanceBuilderType);

        method.getBody().ifPresent(body -> body.addAndGetStatement(new ReturnStmt().setExpression(instanceBuilder)));
        anonymousClassBody.add(method);
    }

    private Expression getFieldAccessor(PropertyDefinition field) {
        if (typeUtils.hasSetter(field.getProperty())) {
            return new MethodCallExpr(
                    new NameExpr("bean"), typeUtils.getSetter(field.getProperty()).getSimpleName().toString()).addArgument("value");
        } else {
            return new AssignExpr().setTarget(new FieldAccessExpr(new NameExpr("bean"), field.getProperty().getSimpleName().toString()))
                    .setValue(new NameExpr("value"));
        }
    }

    private void addParameter(MethodDeclaration method, String type, String name) {
        method.addParameter(new ClassOrInterfaceType().setName(type), name);
    }
}
