package com.gooddata.qa.utils.http.attribute;

import com.gooddata.qa.utils.http.CommonRestRequest;
import com.gooddata.qa.utils.http.RestClient;
import com.gooddata.qa.utils.http.RestRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

import java.io.IOException;

public class AttributeRestRequest extends CommonRestRequest {
    private static final String SET_ATTRIBUTE_PROTECT_TEMPLATE = "{\"setProtected\": {\"items\": [%s]}}";
    private static final String SET_ATTRIBUTE_DRILL_DOWN_TEMPLATE = "{\"drillDownStepAttributeDF\": \"%s\"}";
    private static final String SET_PROTECT_API_TEMPLATE = "/gdc/md/%s/objects/setFlag/protected";

    private static final String UNSET_PROTECT_API_TEMPLATE = "/gdc/md/%s/objects/unsetFlag/protected";
    private static final String UNSET_ATTRIBUTE_PROTECT_TEMPLATE = "{\"unsetProtected\": {\"items\": [%s]}}";

    private static final String QUOTE_TEMPLATE = "\"%s\"";
    private static final String OBJ_LINK = "/gdc/md/%s/obj/";

    public AttributeRestRequest(RestClient restClient, String projectId) {
        super(restClient, projectId);
    }

    /**
     * Set flag 'protected' to attribute
     * 
     * @param attributesUri
     */
    public void setAttributeProtected(String... attributesUri) {
        restClient.execute(RestRequest.initPostRequest(format(SET_PROTECT_API_TEMPLATE, projectId),
                format(SET_ATTRIBUTE_PROTECT_TEMPLATE, quoteUris(attributesUri))), HttpStatus.NO_CONTENT);
    }

    /**
     * Set drill down attribute
     * @param attributeTitle
     * @param attributesDrillToUri
     * @throws IOException
     * @throws JSONException
     */
    public void setDrillDown(String attributeTitle, String attributesDrillToUri) throws JSONException, IOException {
        String uri = getAttributeByTitle(attributeTitle).getUri();
        JSONObject json = getJsonObject(uri);
        JSONObject content = json.getJSONObject("attribute").getJSONObject("content");
        content.put("drillDownStepAttributeDF", attributesDrillToUri);
        executeRequest(RestRequest.initPutRequest(uri, json.toString()), HttpStatus.OK);
    }

    /**
     * Unset flag 'protected' to attribute
     * @param attributesUri
     */
    public void unsetAttributeProtected(String... attributesUri) {
        restClient.execute(
                RestRequest.initPostRequest(
                        format(UNSET_PROTECT_API_TEMPLATE, projectId),
                        format(UNSET_ATTRIBUTE_PROTECT_TEMPLATE, quoteUris(attributesUri))),
                HttpStatus.NO_CONTENT);
    }

    public void setElementMaskingForAttribute(final String attributeUri, final String maskingMetricUri,
                                                final MaskNames maskName, final String maskingObjectTitle) {
        executeRequest(RestRequest.initPostRequest(format(OBJ_LINK, projectId),
            new JSONObject() {{
                put("elementMasking", new JSONObject() {{
                    put("content", new JSONObject() {{
                        put("attribute", attributeUri);
                        put("maskingMetric", maskingMetricUri);
                        put("maskValue", maskName);
                    }});
                    put("meta", new JSONObject() {{
                        put("title", maskingObjectTitle);
                    }});
                }});
            }}.toString()), HttpStatus.OK);
    }

    public enum MaskNames {
        HIDDEN, UNDISCLOSED
    }

    private static String quoteUris (String... uris) {
        return Stream.of(uris)
                .map(uri -> String.format(QUOTE_TEMPLATE, uri))
                .collect(Collectors.joining(","));
    }
}
