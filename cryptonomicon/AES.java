package cryptonomicon;

public class AES {
    // S盒和逆S盒定义
    private static final int[][] S = {
            {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
            {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
            {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
            {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
            {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
            {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
            {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
            {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
            {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
            {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
            {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
            {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
            {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
            {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
            {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
            {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}
    };

    private static final int[][] S2 = {
            {0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb},
            {0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb},
            {0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e},
            {0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25},
            {0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92},
            {0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84},
            {0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06},
            {0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b},
            {0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73},
            {0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e},
            {0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b},
            {0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4},
            {0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f},
            {0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef},
            {0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61},
            {0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d}
    };

    private static final int[] Rcon = {
            0x01000000, 0x02000000, 0x04000000, 0x08000000,
            0x10000000, 0x20000000, 0x40000000, 0x80000000,
            0x1b000000, 0x36000000
    };

    private final int[] w = new int[44]; // 密钥扩展数组

    // 获取高4位
    private int getLeft4Bit(int num) {
        return (num >> 4) & 0x0F;
    }

    // 获取低4位
    private int getRight4Bit(int num) {
        return num & 0x0F;
    }

    // 字节代换
    private int subByte(int b) {
        int row = getLeft4Bit(b);
        int col = getRight4Bit(b);
        return S[row][col];
    }

    // 逆字节代换
    private int deSubByte(int b) {
        int row = getLeft4Bit(b);
        int col = getRight4Bit(b);
        return S2[row][col];
    }

    // 字符串转4x4矩阵（列优先）
    private void strToMatrix(byte[] data, int[][] matrix) {
        int index = 0;
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                matrix[row][col] = data[index++] & 0xFF;
            }
        }
    }

    // 4x4矩阵转字符串（列优先）
    private void matrixToStr(int[][] matrix, byte[] data) {
        int index = 0;
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                data[index++] = (byte) matrix[row][col];
            }
        }
    }

    // 合并4字节为整数（大端序）
    private int bytesToInt(byte[] data, int offset) {
        return ((data[offset] & 0xFF) << 24) |
                ((data[offset+1] & 0xFF) << 16) |
                ((data[offset+2] & 0xFF) << 8) |
                (data[offset+3] & 0xFF);
    }

    // 分解整数为4字节（大端序）
    private void intToBytes(int num, byte[] bytes) {
        bytes[0] = (byte) ((num >> 24) & 0xFF);
        bytes[1] = (byte) ((num >> 16) & 0xFF);
        bytes[2] = (byte) ((num >> 8) & 0xFF);
        bytes[3] = (byte) (num & 0xFF);
    }

    // 循环左移数组
    private void leftRotate(int[] arr, int shift) {
        shift %= 4;
        int[] temp = new int[4];
        System.arraycopy(arr, shift, temp, 0, 4 - shift);
        System.arraycopy(arr, 0, temp, 4 - shift, shift);
        System.arraycopy(temp, 0, arr, 0, 4);
    }

    // 密钥扩展函数
    private void keyExpansion(byte[] key) {
        for (int i = 0; i < 4; i++) {
            int word = bytesToInt(key, i * 4);
            w[i] = word;
        }

        for (int i = 4, rconIdx = 0; i < 44; i++) {
            int temp = w[i-1];
            if (i % 4 == 0) {
                // 字循环
                // 将 int 转换为 int 数组
                int[] tempInts = new int[4];
                byte[] tempBytes = new byte[4];
                intToBytes(temp, tempBytes);
                for (int j = 0; j < 4; j++) {
                    tempInts[j] = tempBytes[j] & 0xFF;
                }
                // 调用 leftRotate 方法传入 int 数组
                leftRotate(tempInts, 1);
                for (int j = 0; j < 4; j++) {
                    tempBytes[j] = (byte) tempInts[j];
                }
                temp = bytesToInt(tempBytes, 0);

                // 字节代换
                tempBytes = new byte[4];
                intToBytes(temp, tempBytes);
                for (int j = 0; j < 4; j++) {
                    tempBytes[j] = (byte) subByte(tempBytes[j] & 0xFF);
                }
                temp = bytesToInt(tempBytes, 0);

                // 轮常量异或
                temp ^= Rcon[rconIdx++];
            }
            w[i] = w[i-4] ^ temp;
        }
    }

    // 轮密钥加
    private void addRoundKey(int[][] state, int round) {
        for (int col = 0; col < 4; col++) {
            int word = w[round * 4 + col];
            byte[] bytes = new byte[4];
            intToBytes(word, bytes);
            for (int row = 0; row < 4; row++) {
                state[row][col] ^= (bytes[row] & 0xFF);
            }
        }
    }

    // 行移位
    private void shiftRows(int[][] state) {
        // 行1: 左移1
        int[] row1 = new int[4];
        System.arraycopy(state[1], 0, row1, 0, 4);
        leftRotate(row1, 1);
        System.arraycopy(row1, 0, state[1], 0, 4);

        // 行2: 左移2
        int[] row2 = new int[4];
        System.arraycopy(state[2], 0, row2, 0, 4);
        leftRotate(row2, 2);
        System.arraycopy(row2, 0, state[2], 0, 4);

        // 行3: 左移3
        int[] row3 = new int[4];
        System.arraycopy(state[3], 0, row3, 0, 4);
        leftRotate(row3, 3);
        System.arraycopy(row3, 0, state[3], 0, 4);
    }

    // 逆行移位
    private void deShiftRows(int[][] state) {
        // 行1: 右移1（等价于左移3）
        int[] row1 = new int[4];
        System.arraycopy(state[1], 0, row1, 0, 4);
        leftRotate(row1, 3);
        System.arraycopy(row1, 0, state[1], 0, 4);

        // 行2: 右移2（等价于左移2）
        int[] row2 = new int[4];
        System.arraycopy(state[2], 0, row2, 0, 4);
        leftRotate(row2, 2);
        System.arraycopy(row2, 0, state[2], 0, 4);

        // 行3: 右移3（等价于左移1）
        int[] row3 = new int[4];
        System.arraycopy(state[3], 0, row3, 0, 4);
        leftRotate(row3, 1);
        System.arraycopy(row3, 0, state[3], 0, 4);
    }

    // GF(2^8)乘法
    private int gfMul(int a, int b) {
        int p = 0;
        for (int i = 0; i < 8; i++) {
            if ((b & 1) != 0) {
                p ^= a;
            }
            boolean highBitSet = (a & 0x80) != 0;
            a <<= 1;
            if (highBitSet) {
                a ^= 0x1B; // x^8 + x^4 + x^3 + x + 1
            }
            b >>= 1;
        }
        return p & 0xFF;
    }

    // 列混合
    private void mixColumns(int[][] state) {
        int[][] temp = new int[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(state[i], 0, temp[i], 0, 4);
        }

        for (int col = 0; col < 4; col++) {
            state[0][col] = gfMul(2, temp[0][col]) ^ gfMul(3, temp[1][col]) ^ temp[2][col] ^ temp[3][col];
            state[1][col] = temp[0][col] ^ gfMul(2, temp[1][col]) ^ gfMul(3, temp[2][col]) ^ temp[3][col];
            state[2][col] = temp[0][col] ^ temp[1][col] ^ gfMul(2, temp[2][col]) ^ gfMul(3, temp[3][col]);
            state[3][col] = gfMul(3, temp[0][col]) ^ temp[1][col] ^ temp[2][col] ^ gfMul(2, temp[3][col]);
        }
    }

    // 逆列混合
    private void deMixColumns(int[][] state) {
        int[][] temp = new int[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(state[i], 0, temp[i], 0, 4);
        }

        for (int col = 0; col < 4; col++) {
            state[0][col] = gfMul(0xE, temp[0][col]) ^ gfMul(0xB, temp[1][col]) ^ gfMul(0xD, temp[2][col]) ^ gfMul(0x9, temp[3][col]);
            state[1][col] = gfMul(0x9, temp[0][col]) ^ gfMul(0xE, temp[1][col]) ^ gfMul(0xB, temp[2][col]) ^ gfMul(0xD, temp[3][col]);
            state[2][col] = gfMul(0xD, temp[0][col]) ^ gfMul(0x9, temp[1][col]) ^ gfMul(0xE, temp[2][col]) ^ gfMul(0xB, temp[3][col]);
            state[3][col] = gfMul(0xB, temp[0][col]) ^ gfMul(0xD, temp[1][col]) ^ gfMul(0x9, temp[2][col]) ^ gfMul(0xE, temp[3][col]);
        }
    }

    // 字节代换（整个状态）
    private void subBytes(int[][] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = subByte(state[i][j]);
            }
        }
    }

    // 逆字节代换（整个状态）
    private void deSubBytes(int[][] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = deSubByte(state[i][j]);
            }
        }
    }

    // ECB加密
    public void aesEncryptECB(byte[] plaintext, byte[] ciphertext, byte[] key) {
        int[][] state = new int[4][4];
        strToMatrix(plaintext, state);
        keyExpansion(key);
        addRoundKey(state, 0);

        for (int round = 1; round < 10; round++) {
            subBytes(state);
            shiftRows(state);
            mixColumns(state);
            addRoundKey(state, round);
        }

        subBytes(state);
        shiftRows(state);
        addRoundKey(state, 10);
        matrixToStr(state, ciphertext);
    }

    // ECB解密
    public void aesDecryptECB(byte[] ciphertext, byte[] plaintext, byte[] key) {
        int[][] state = new int[4][4];
        strToMatrix(ciphertext, state);
        keyExpansion(key);
        addRoundKey(state, 10);

        for (int round = 9; round >= 1; round--) {
            deShiftRows(state);
            deSubBytes(state);
            addRoundKey(state, round);
            deMixColumns(state);
        }

        deShiftRows(state);
        deSubBytes(state);
        addRoundKey(state, 0);
        matrixToStr(state, plaintext);
    }

    // CBC加密
    public void aesCbcEncrypt(byte[] plaintext, byte[] ciphertext, byte[] key, byte[] iv) {
        byte[] prevBlock = iv.clone();
        for (int i = 0; i < plaintext.length; i += 16) {
            byte[] block = new byte[16];
            for (int j = 0; j < 16; j++) {
                block[j] = (byte) (plaintext[i+j] ^ prevBlock[j]);
            }
            aesEncryptECB(block, block, key);
            System.arraycopy(block, 0, ciphertext, i, 16);
            prevBlock = block;
        }
    }

    // CBC解密
    public void aesCbcDecrypt(byte[] ciphertext, byte[] plaintext, byte[] key, byte[] iv) {
        byte[] prevBlock = iv.clone();
        for (int i = 0; i < ciphertext.length; i += 16) {
            byte[] block = new byte[16];
            System.arraycopy(ciphertext, i, block, 0, 16);
            byte[] decryptedBlock = new byte[16];
            aesDecryptECB(block, decryptedBlock, key);
            for (int j = 0; j < 16; j++) {
                plaintext[i+j] = (byte) (decryptedBlock[j] ^ prevBlock[j]);
            }
            prevBlock = block; // 使用原始密文块作为下一轮的IV
        }
    }

    // 零填充
    public static byte[] padZero(byte[] data) {
        int padLen = 16 - (data.length % 16);
        if (padLen == 16) padLen = 0;
        byte[] padded = new byte[data.length + padLen];
        System.arraycopy(data, 0, padded, 0, data.length);
        return padded;
    }

    // 去零填充
    public static byte[] unpadZero(byte[] data) {
        int len = data.length;
        while (len > 0 && data[len-1] == 0) len--;
        return java.util.Arrays.copyOf(data, len);
    }


    public static byte[] aesCbcEncryptWrapper(String plaintext, String key, String iv) {
        AES aes = new AES();
        byte[] plainBytes = plaintext.getBytes();
        byte[] paddedBytes = padZero(plainBytes);
        byte[] keyBytes = key.getBytes();
        byte[] ivBytes = iv.getBytes();
        byte[] ciphertext = new byte[paddedBytes.length];
        aes.aesCbcEncrypt(paddedBytes, ciphertext, keyBytes, ivBytes);
        return ciphertext;
    }

    /**
     * AES CBC 模式解密方法
     * @param ciphertext 密文
     * @param key 16 字节密钥
     * @param iv 16 字节初始化向量
     * @return 解密后的明文字符串
     */
    public static String aesCbcDecryptWrapper(byte[] ciphertext, String key, String iv) {
        AES aes = new AES();
        byte[] keyBytes = key.getBytes();
        byte[] ivBytes = iv.getBytes();
        byte[] decryptedBytes = new byte[ciphertext.length];
        aes.aesCbcDecrypt(ciphertext, decryptedBytes, keyBytes, ivBytes);
        byte[] unpaddedBytes = unpadZero(decryptedBytes);
        return new String(unpaddedBytes);
    }

}
