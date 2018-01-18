package com.somelogs.javase.javap.attribute;

import com.somelogs.javase.javap.constantpool.ConstantPool;
import com.somelogs.javase.javap.datatype.U1;
import com.somelogs.javase.javap.datatype.U2;
import com.somelogs.javase.javap.datatype.U4;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Attribute: Code
 *
 * @author LBG - 2018/1/17 0017
 */
@Setter
@Getter
@ToString
public class CodeAttrInfo extends AttributeInfo {

    private int maxStack;
    private int maxLocals;
    private int codeLength;
    private String[] opcodes;
    private List<ExceptionInfo> exceptionInfoList;
    private List<AttributeInfo> attributeInfoList;

    @Override
    public void readMore(InputStream inputStream) {
        U2 maxStackU2 = U2.read(inputStream);
        maxStack = maxStackU2.getValue();
        U2 maxLocalsU2 = U2.read(inputStream);
        maxLocals = maxLocalsU2.getValue();

        U4 codeLengthU4 = U4.read(inputStream);
        codeLength = codeLengthU4.getValue();
        opcodes = new String[codeLength];
        if (codeLength > 0) {
            for (int i = 0; i < codeLength; i++) {
                U1 opcode = U1.read(inputStream);
                opcodes[i] = opcode.getHexValue();
            }
        }

        U2 exceptionTable = U2.read(inputStream);
        exceptionInfoList = new ArrayList<>(exceptionTable.getValue());
        if (exceptionInfoList.size() > 0) {
            ExceptionInfo exceptionInfo = new ExceptionInfo();
            U2 startPcU2 = U2.read(inputStream);
            U2 endPcU2 = U2.read(inputStream);
            U2 handlerU2 = U2.read(inputStream);
            U2 typeU2 = U2.read(inputStream);
            exceptionInfo.setStart(startPcU2.getValue());
            exceptionInfo.setEnd(endPcU2.getValue());
            exceptionInfo.setHandler(handlerU2.getValue());
            exceptionInfo.setType(typeU2.getValue());
            exceptionInfoList.add(exceptionInfo);
        }

        U2 attrCountU2 = U2.read(inputStream);
        attributeInfoList = new ArrayList<>(attrCountU2.getValue());
        if (attrCountU2.getValue() >0) {
            String attrName = ConstantPool.getStringByIndex(U2.read(inputStream).getValue());
            AttributeInfo attrInfo = AttributeInfo.getAttrByName(attrName);
            attrInfo.readMore(inputStream);
            attributeInfoList.add(attrInfo);
        }
    }

    @Getter
    @Setter
    @ToString
    private class ExceptionInfo {
        private int start;
        private int end;
        private int handler;
        private int type;
    }
}