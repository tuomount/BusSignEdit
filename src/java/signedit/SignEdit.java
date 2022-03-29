/**
 * Copyright © 2022 Tuomo Untinen
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the “Software”),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR 
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package signedit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SignEdit {

  public static boolean getBit(byte value, int index) {
    byte mask = 0;
    switch (index) {
    case 0: {
      mask = (byte) 0x80;
      break;
    }
    case 1: {
      mask = (byte) 0x40;
      break;
    }
    case 2: {
      mask = (byte) 0x20;
      break;
    }
    case 3: {
      mask = (byte) 0x10;
      break;
    }
    case 4: {
      mask = (byte) 0x08;
      break;
    }
    case 5: {
      mask = (byte) 0x04;
      break;
    }
    case 6: {
      mask = (byte) 0x02;
      break;
    }
    case 7: {
      mask = (byte) 0x01;
      break;
    }
    }
    if ((value & mask) == mask) {
      return true;
    }
    return false;
  }

  public static byte setBit(byte value, int index) {
    byte mask = 0;
    switch (index) {
    case 0: {
      mask = (byte) 0x80;
      break;
    }
    case 1: {
      mask = (byte) 0x40;
      break;
    }
    case 2: {
      mask = (byte) 0x20;
      break;
    }
    case 3: {
      mask = (byte) 0x10;
      break;
    }
    case 4: {
      mask = (byte) 0x08;
      break;
    }
    case 5: {
      mask = (byte) 0x04;
      break;
    }
    case 6: {
      mask = (byte) 0x02;
      break;
    }
    case 7: {
      mask = (byte) 0x01;
      break;
    }
    }
    return (byte) (value | mask);
  }

  public static String readFullCharacter(String filename)
      throws FileNotFoundException, IOException {
    try (BufferedInputStream bis = new BufferedInputStream(
        new FileInputStream(new File(filename)))) {
      // one kilo buffer
      int bufferSize = 1024;
      byte[] buffer = new byte[bufferSize];
      int offset = 0;
      int readAmount = 0;
      int left = bufferSize;
      do {
        readAmount = bis.read(buffer, offset, left);
        if (readAmount > 0) {
          offset = offset + readAmount;
          left = left - readAmount;
        }
      } while (left > 0 && readAmount > 1);
      String strBuffer = new String(buffer, 0, offset,
          StandardCharsets.US_ASCII);
      return strBuffer;
    }

  }
  public static void main(String[] args) {
    if (args.length == 3) {
      int width = Integer.parseInt(args[0]);
      int height = Integer.parseInt(args[1]);
      String data = args[2];
      byte[] buffer = Base64.getDecoder().decode(data);
      int bait = 0;
      int bit = 0;
      for (int y = 0; y < height; y++) {
        StringBuilder row = new StringBuilder();
        for (int x = 0; x < width; x++) {
          if (bait < buffer.length) {
            byte value = buffer[bait];
            if (getBit(value, bit)) {
              row.append('1');
            } else {
              row.append('0');
            }
          } else {
            System.out.println("too short buffer");
          }
          bit++;
          if (bit == 8) {
            bit = 0;
            bait++;
          }
        }
        System.out.println(row.toString());
      }
    } else if (args.length == 1) {
      String data;
      try {
        data = readFullCharacter(args[0]);
        String[] lines = data.split("\n");
        int height = lines.length;
        int width = lines[0].length();
        int size = height * width / 8;
        byte[] buffer = new byte[size];
        int bait = 0;
        int bit = 0;
        for (int y = 0; y < height; y++) {
          for (int x = 0; x < width; x++) {
            if (lines[y].charAt(x) == '1') {
              buffer[bait] = setBit(buffer[bait], bit);
            }
            bit++;
            if (bit == 8) {
              bait++;
              bit = 0;
            }
          }
        }
        System.out.println("Width=\"" + width +"\" height=\"" + height +
            "\" data=\"" + Base64.getEncoder().encodeToString(buffer) + "\"");
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } else {
      System.out.println("Usage: WIDTH HEIGHT DATA");
      return;
    }

  }

}

