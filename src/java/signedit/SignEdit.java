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
    } else {
      System.out.println("Usage: WIDTH HEIGHT DATA");
      return;
    }

  }

}

