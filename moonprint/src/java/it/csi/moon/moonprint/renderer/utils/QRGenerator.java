/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.renderer.utils;

import java.awt.image.BufferedImage;

import io.nayuki.qrcodegen.QrCode;
import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


import java.io.IOException;

public class QRGenerator {

    public static String generateQR(String txt) {

    int scale = 20;
    int border = 0;
    String fileType = "png";

    QrCode qr0 = QrCode.encodeText(txt, QrCode.Ecc.MEDIUM);
    BufferedImage img = qr0.toImage(scale, border);

    return imgToBase64String(img, fileType);
}


    public static String imgToBase64String(final RenderedImage img, final String formatName) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, formatName, Base64.getEncoder().wrap(os));
            return os.toString(StandardCharsets.ISO_8859_1.name());
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }
}
