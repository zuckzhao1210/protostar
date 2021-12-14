package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.PrepareLoadStrategy;
import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.IntTool;
import io.github.yezhihao.protostar.util.KeyValuePair;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Map.Entry;

public abstract class MapSchema<K, V> implements Schema<Entry<K, V>> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    protected final Schema<K> keySchema;
    protected final int lengthSize;
    protected final IntTool intTool;
    protected final Map<K, Schema> valueSchema;

    public MapSchema(Schema<K> keySchema, int lengthSize) {
        this.keySchema = keySchema;
        this.lengthSize = lengthSize;
        this.intTool = IntTool.getInstance(lengthSize);
        PrepareLoadStrategy<K> loadStrategy = new PrepareLoadStrategy<>();
        addSchemas(loadStrategy);
        this.valueSchema = loadStrategy.build();
    }

    protected abstract void addSchemas(PrepareLoadStrategy<K> schemaRegistry);

    @Override
    public KeyValuePair<K, V> readFrom(ByteBuf input) {
        K key = keySchema.readFrom(input);
        KeyValuePair<K, V> result = new KeyValuePair<>(key);

        int length = intTool.read(input);
        if (length > 0) {
            int writerIndex = input.writerIndex();
            input.writerIndex(input.readerIndex() + length);

            Schema<V> schema = valueSchema.get(key);
            if (schema != null) {
                V value = schema.readFrom(input, length);
                result.setValue(value);
            } else {
                byte[] bytes = new byte[length];
                input.readBytes(bytes);
                result.setValue((V) bytes);
            }
            input.writerIndex(writerIndex);

        } else if (length < 0) {
            Schema<V> schema = valueSchema.get(key);
            if (schema != null) {
                V value = schema.readFrom(input);
                result.setValue(value);
            } else {
                byte[] bytes = new byte[input.readableBytes()];
                input.readBytes(bytes);
                result.setValue((V) bytes);
            }
        }
        return result;
    }

    @Override
    public void writeTo(ByteBuf output, Entry<K, V> entry) {
        K key = entry.getKey();
        keySchema.writeTo(output, key);

        Schema<V> schema = valueSchema.get(key);
        if (schema != null) {
            int begin = output.writerIndex();
            intTool.write(output, 0);

            V value = entry.getValue();
            if (value != null) {
                schema.writeTo(output, value);
                int length = output.writerIndex() - begin - lengthSize;
                intTool.set(output, begin, length);
            }
        } else {
            log.warn("未注册的信息:Key[{}], Value[{}]", key, entry.getValue());
        }
    }

    @Override
    public KeyValuePair<K, V> readFrom(ByteBuf input, Explain explain) {
        K key = keySchema.readFrom(input, explain);
        explain.setLastDesc("key");
        KeyValuePair<K, V> result = new KeyValuePair<>(key);

        int length = intTool.read(input);
        if (length <= 0)
            return result;

        int writerIndex = input.writerIndex();
        input.writerIndex(input.readerIndex() + length);

        Schema<V> schema = valueSchema.get(key);
        if (schema != null) {
            V value = schema.readFrom(input, length, explain);
            result.setValue(value);
        } else {
            byte[] bytes = new byte[length];
            input.readBytes(bytes);
            result.setValue((V) bytes);
        }
        input.writerIndex(writerIndex);
        return result;
    }

    @Override
    public void writeTo(ByteBuf output, Entry<K, V> entry, Explain explain) {
        K key = entry.getKey();
        keySchema.writeTo(output, key, explain);
        explain.setLastDesc("key");

        Schema schema = valueSchema.get(key);
        if (schema != null) {
            int begin = output.writerIndex();
            intTool.write(output, 0);

            Object value = entry.getValue();
            if (value != null) {
                schema.writeTo(output, value, explain);
                int length = output.writerIndex() - begin - lengthSize;
                intTool.set(output, begin, length);
            }
        } else {
            log.warn("未注册的信息:Key[{}], Value[{}]", key, entry.getValue());
        }
    }
}