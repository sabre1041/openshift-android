package com.openshift.android.marshall;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.openshift.android.model.OpenshiftDataList;

@SuppressWarnings({"rawtypes","unchecked"})
public class OpenshiftDataListDeserializer implements
		JsonDeserializer<OpenshiftDataList> {

	@Override
	public OpenshiftDataList deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		if (typeOfT instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) typeOfT;
			Type type = pt.getActualTypeArguments()[0];

			JsonArray array = json.getAsJsonArray();

			OpenshiftDataList serializedList = new OpenshiftDataList();

			List list = new ArrayList();

			for (JsonElement element : array) {
				Object o = context.deserialize(element, type);
				list.add(o);
			}

			serializedList.setList(list);

			return serializedList;

		}

		return null;
	}

}
