package com.voicepin.flow.client.util;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;

/**
 * @author mckulpa
 */
public class BodyPartFactory {

    private BodyPartFactory() {
    }

    public static FormDataBodyPart createOctetStreamBodyPart(String name, InputStream stream) {
        FormDataContentDisposition contentDisposition =
                FormDataContentDisposition.name(name).fileName(name).build();

        FormDataBodyPart bodyPart = new FormDataBodyPart(contentDisposition, stream,
                MediaType.APPLICATION_OCTET_STREAM_TYPE);
        return bodyPart;
    }

    public static FormDataBodyPart createTextPlainBodyPart(String name, InputStream stream) {
        FormDataContentDisposition contentDisposition =
                FormDataContentDisposition.name(name).fileName(name).build();

        FormDataBodyPart bodyPart = new FormDataBodyPart(contentDisposition, stream,
                MediaType.TEXT_PLAIN_TYPE);
        return bodyPart;
    }
}
