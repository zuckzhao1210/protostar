package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.netty.buffer.ByteBuf;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class ArraySchema {

    public static final Schema<char[]> CHARS = new CharArray();
    public static final Schema<byte[]> BYTES = new ByteArray();
    public static final Schema<short[]> SHORTS = new ShortArray();
    public static final Schema<int[]> INTS = new IntArray();
    public static final Schema<float[]> FLOATS = new FloatArray();
    public static final Schema<long[]> LONGS = new LongArray();
    public static final Schema<double[]> DOUBLES = new DoubleArray();

    private static class ByteArray implements Schema<byte[]> {
        @Override
        public byte[] readFrom(ByteBuf input) {
            byte[] array = new byte[input.readableBytes()];
            input.readBytes(array);
            return array;
        }

        @Override
        public byte[] readFrom(ByteBuf input, int length) {
            if (length < 0)
                length = input.readableBytes();
            byte[] array = new byte[length];
            input.readBytes(array);
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, byte[] array) {
            output.writeBytes(array);
        }

        @Override
        public void writeTo(ByteBuf output, int length, byte[] array) {
            output.writeBytes(array, 0, length);
        }
    }

    private static class CharArray implements Schema<char[]> {
        @Override
        public char[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 1;
            char[] array = new char[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readChar();
            return array;
        }

        @Override
        public char[] readFrom(ByteBuf input, int length) {
            if (length < 0)
                length = input.readableBytes();
            int total = length >> 1;
            char[] array = new char[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readChar();
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, char[] array) {
            for (int i = 0; i < array.length; i++) {
                output.writeChar(array[i]);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, char[] array) {
            for (int i = 0, total = length >> 1; i < total; i++) {
                output.writeChar(array[i]);
            }
        }
    }

    private static class ShortArray implements Schema<short[]> {
        @Override
        public short[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 1;
            short[] array = new short[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readShort();
            return array;
        }

        @Override
        public short[] readFrom(ByteBuf input, int length) {
            if (length < 0)
                length = input.readableBytes();
            int total = length >> 1;
            short[] array = new short[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readShort();
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, short[] array) {
            for (int i = 0; i < array.length; i++) {
                output.writeShort(array[i]);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, short[] array) {
            for (int i = 0, total = length >> 1; i < total; i++) {
                output.writeShort(array[i]);
            }
        }
    }

    private static class IntArray implements Schema<int[]> {
        @Override
        public int[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 2;
            int[] array = new int[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readInt();
            return array;
        }

        @Override
        public int[] readFrom(ByteBuf input, int length) {
            if (length < 0)
                length = input.readableBytes();
            int total = length >> 2;
            int[] array = new int[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readInt();
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, int[] array) {
            for (int i = 0; i < array.length; i++) {
                output.writeInt(array[i]);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, int[] array) {
            for (int i = 0, total = length >> 2; i < total; i++) {
                output.writeInt(array[i]);
            }
        }
    }

    private static class LongArray implements Schema<long[]> {
        @Override
        public long[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 3;
            long[] array = new long[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readLong();
            return array;
        }

        @Override
        public long[] readFrom(ByteBuf input, int length) {
            if (length < 0)
                length = input.readableBytes();
            int total = length >> 3;
            long[] array = new long[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readLong();
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, long[] array) {
            for (int i = 0; i < array.length; i++) {
                output.writeLong(array[i]);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, long[] array) {
            for (int i = 0, total = length >> 3; i < total; i++) {
                output.writeLong(array[i]);
            }
        }
    }

    private static class FloatArray implements Schema<float[]> {
        @Override
        public float[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 2;
            float[] array = new float[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readFloat();
            return array;
        }

        @Override
        public float[] readFrom(ByteBuf input, int length) {
            if (length < 0)
                length = input.readableBytes();
            int total = length >> 2;
            float[] array = new float[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readFloat();
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, float[] array) {
            for (int i = 0; i < array.length; i++) {
                output.writeFloat(array[i]);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, float[] array) {
            for (int i = 0, total = length >> 2; i < total; i++) {
                output.writeFloat(array[i]);
            }
        }
    }

    private static class DoubleArray implements Schema<double[]> {
        @Override
        public double[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 3;
            double[] array = new double[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readDouble();
            return array;
        }

        @Override
        public double[] readFrom(ByteBuf input, int length) {
            if (length < 0)
                length = input.readableBytes();
            int total = length >> 3;
            double[] array = new double[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readDouble();
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, double[] array) {
            for (int i = 0; i < array.length; i++) {
                output.writeDouble(array[i]);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, double[] array) {
            for (int i = 0, total = length >> 3; i < total; i++) {
                output.writeDouble(array[i]);
            }
        }
    }
}