package com.voicepin.flow.client.calls;


import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.RestFieldName;
import com.voicepin.flow.client.exception.FlowParseException;
import com.voicepin.flow.client.request.VerifyStreamRequest;
import com.voicepin.flow.client.result.VerifyStreamResult;
import com.voicepin.flow.client.util.BodyPartFactory;

/**
 * @author kodrzywolek
 */
public class VerifyStreamCall implements Call<VerifyStreamResult> {

    private final VerifyStreamRequest req;

    public VerifyStreamCall(final VerifyStreamRequest verifyStreamRequest) {
        this.req = verifyStreamRequest;
    }

    @Override
    public Method getMethod() {
        return Method.PUT;
    }

    @Override
    public String getPath() {
        return req.getSpeechPath() + "_file";
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public Entity<?> getEntity() {
        final FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        formDataMultiPart.bodyPart(BodyPartFactory.createOctetStreamBodyPart(RestFieldName.MULTIPART_REQUEST_RECORDING,
                req.getSpeechStream()));
        return Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA_TYPE);
    }

    @Override
    public VerifyStreamResult parse(final Response response) throws FlowParseException {
        return response.readEntity(VerifyStreamResult.class);
    }
}
