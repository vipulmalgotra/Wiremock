package com.opentable.extension;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.*;

public class BodyTransformer extends ResponseTransformer {

    Map object = null;
    Gson gsonBuilder = new GsonBuilder().create();
    JsonObject obj;

    @Override
    public Response transform(Request request, Response response, FileSource fileSource, Parameters parameters) {

    if (request.getAbsoluteUrl().split("\\?").length == 2) {

        obj = new JsonObject();
        obj.addProperty("playImage", "https://fancode.com/skillup-uploads/prod-images/2019/08/live-button.png");
        obj.addProperty("playText", "Watch on FanCode");
        obj.addProperty("isVideoAvailable", false);

        object = new HashMap();
        String absoluteUrl = request.getAbsoluteUrl();
        String pairedValues = absoluteUrl.split("\\?")[1];
        String[] values = pairedValues.split("=");
        String[] matchids = values[1].split(",") ;
        for(int i=0; i<matchids.length ; i++)
        {
            object.put(matchids[i],obj);
        }
        }

        String jsonFromJavaMap = gsonBuilder.toJson(object);
        return Response.Builder.like(response)
            .but().body(jsonFromJavaMap)
            .build();
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }

    @Override
    public String getName() {
        return "book-return-date-transformer";
    }
}
