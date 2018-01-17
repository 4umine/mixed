package com.somelogs.javase.javap.attribute;

import lombok.Data;

import java.io.InputStream;

/**
 * attribute info
 *
 * @author LBG - 2018/1/17 0017
 */
@Data
public abstract class AttributeInfo {

    private static final String CODE = "Code";
    private static final String DEPRECATED = "Deprecated";
    private static final String SIGNATURE = "Signature";
    private static final String SYNTHETIC = "Synthetic";
    private static final String EXCEPTIONS = "Exceptions";
    private static final String LINE_NUMBER_TABLE = "LineNumberTable";
    private static final String LOCAL_VARIABLE_TABLE = "LocalVariableTable";
    private static final String SOURCE_FILE = "SourceFile";
    private static final String CONSTANT_VALUE = "ConstantValue";
    private static final String INNER_CLASSES = "InnerClasses";
    private static final String STACK_MAP_TABLE = "StackMapTable";
    private static final String BOOTSTRAP_METHODS = "BootstrapMethods";

    protected String content;

    public abstract void analyze(InputStream inputStream);

    public static AttributeInfo getAttrByName(String attrName) {
        AttributeInfo info;
        switch (attrName) {
            case DEPRECATED:
                info = new DeprecatedAttrInfo();
                break;
            case SIGNATURE:
                info = new SignatureAttrInfo();
                break;
            case SYNTHETIC:
                info = new SyntheticAttrInfo();
                break;
            case CODE:
                info = new CodeAttrInfo();
                break;
            case EXCEPTIONS:
                info = new ExceptionsAttrInfo();
                break;
            case LINE_NUMBER_TABLE:
                info = new LineNumberTableAttrInfo();
                break;
            case LOCAL_VARIABLE_TABLE:
                info = new LocalVariableTableAttrInfo();
                break;
            case SOURCE_FILE:
                info = new SourceFileAttrInfo();
                break;
            case CONSTANT_VALUE:
                info = new ConstantValueAttrInfo();
                break;
            case INNER_CLASSES:
                info = new InnerClassesAttrInfo();
                break;
            case STACK_MAP_TABLE:
                info = new StackMapTableAttrInfo();
                break;
            case BOOTSTRAP_METHODS:
                info = new BootstrapMethodsAttrInfo();
                break;
            default:
                throw new RuntimeException("unknown attr name: " + attrName);
        }
        return info;

    }
}
