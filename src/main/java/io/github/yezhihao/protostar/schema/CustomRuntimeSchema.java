package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.Explain;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zhaozhe
 * @since 2024/11/1 13:56
 */
public class CustomRuntimeSchema<T> implements Schema<T> {

    protected Class<T> typeClass;
    protected Constructor<T> constructor;
    protected Method decoder;
    protected Method encoder;

    public CustomRuntimeSchema(Class<T> typeClass) {
        this.typeClass = typeClass;
        try {
            this.constructor = typeClass.getDeclaredConstructor((Class[]) null);
            this.constructor.setAccessible(true);
            this.decoder = typeClass.getMethod("decode", ByteBuf.class);
            this.encoder = typeClass.getMethod("encode");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public T newInstance() {
        try {
            return constructor.newInstance((Object[]) null);
        } catch (Exception e) {
            throw new RuntimeException("newInstance failed " + typeClass.getName(), e);
        }
    }

    public T mergeFrom(ByteBuf input, T result) {
        if (!input.isReadable()) {
            return null;
        }
        try {
            decoder.invoke(result, input);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Merge message failed " + typeClass.getName(), e);
        }
        return result;
    }

    public T mergeFrom(ByteBuf input, T result, Explain explain) {
        if (!input.isReadable()) {
            return null;
        }
        try {
            if (explain == null)
                return mergeFrom(input, result);
            decoder.invoke(result, input);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Merge message failed " + typeClass.getName(), e);
        }
        return result;
    }


    @Override
    public T readFrom(ByteBuf input) {
        if (!input.isReadable())
            return null;
        T message;
        try {
            message = this.newInstance();
            decoder.invoke(message, input);
        } catch ( IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException e) {
            throw new RuntimeException("ReadFrom failed " + typeClass.getName(), e);
        }
        return message;
    }

    @Override
    public void writeTo(ByteBuf output, T value) {
        if (value == null)
            return;
        try {
            encoder.invoke(value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException("WriteTo failed " + typeClass.getName(), e);
        }
    }

    @Override
    public void writeTo(ByteBuf output, T value, Explain explain) {
        if (value == null)
            return;
        if (explain == null) {
            writeTo(output, value);
            return;
        }
        try {
            encoder.invoke(value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException("WriteTo failed " + typeClass.getName(), e);
        }
    }

    public Class<T> typeClass() {
        return typeClass;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(48);
        sb.append("{typeClass=").append(typeClass.getSimpleName());
        sb.append('}');
        return sb.toString();
    }
}
