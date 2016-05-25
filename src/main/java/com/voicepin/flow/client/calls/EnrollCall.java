package com.voicepin.flow.client.calls;


import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.ParsedResponse;
import com.voicepin.flow.client.RestFieldName;
import com.voicepin.flow.client.exception.FlowParseException;
import com.voicepin.flow.client.request.EnrollRequest;
import com.voicepin.flow.client.result.EnrollResult;
import com.voicepin.flow.client.util.BodyPartFactory;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author mckulpa, kodrzywolek
 */
public class EnrollCall implements Call<EnrollResult> {
    private final EnrollRequest req;

    public EnrollCall(EnrollRequest enrollRequest) {
        this.req = enrollRequest;
    }

    @Override
    public Method getMethod() {
        return Method.POST;
    }

    @Override
    public String getPath() {
        return "voiceprint/" + req.getVoiceprintId() + "/enrollment_file";
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public Entity<?> getEntity() {
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        formDataMultiPart.bodyPart(BodyPartFactory.createOctetStreamBodyPart(RestFieldName.MULTIPART_REQUEST_RECORDING,
                req.getSpeechStream()));
        formDataMultiPart.field(RestFieldName.MULTIPART_REQUEST_VOICEPRINT_ID, req.getVoiceprintId());
        return Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA_TYPE);
    }

    @Override
    public ParsedResponse<EnrollResult> parse(Response response) throws FlowParseException {
        return EnrollResult::new;
    }
}
