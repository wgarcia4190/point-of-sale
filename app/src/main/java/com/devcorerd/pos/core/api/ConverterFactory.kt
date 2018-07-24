package com.devcorerd.pos.core.api

import com.devcorerd.pos.helper.ConstantsHelper
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Created by wgarcia on 4/25/2018.
 */
class ConverterFactory : Converter.Factory() {
    private val MEDIA_TYPE = MediaType.parse(ConstantsHelper.applicationJson)

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?,
                                      methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {

        return if (String::class.java == type) {
            Converter<String, RequestBody> { value -> RequestBody.create(MEDIA_TYPE, value) }
        } else null
    }

}