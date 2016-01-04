package at.jku.esh.fishbone.web;

import com.google.common.base.MoreObjects;
import feign.Feign;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
* Created by feb on 07.12.2015.
*/
public class WebClientBuilder<T extends WebClientBuilder.WebClient> {

   private final Class<T> type;
   private final String baseUrl;
   private ErrorDecoder errorDecoder;

   public static <C extends WebClient> WebClientBuilder<C> get(Class<C> type, String baseUrl) {
       return new WebClientBuilder<>(type, baseUrl);
   }

   private WebClientBuilder(Class<T> type, String baseUrl) {
       this.type = type;
       this.baseUrl = baseUrl;
   }

   public WebClientBuilder<T> errorDecoder(ErrorDecoder errorDecoder) {
       this.errorDecoder = errorDecoder;
       return this;
   }

   public T build() {

       JacksonDecoder jacksonDecoder = new JacksonDecoder();
       return Feign.builder()
               .encoder(new JacksonEncoder())
               .decoder(jacksonDecoder)
               .errorDecoder(MoreObjects.firstNonNull(errorDecoder, new WebErrorDecoder(jacksonDecoder)))
               .logger(new Slf4jLogger(type))
               .target(type, requireNonNull(baseUrl));
   }

   public interface WebClient {

   }

   static class WebErrorDecoder implements ErrorDecoder {

       final Decoder decoder;
       final ErrorDecoder defaultDecoder = new Default();

       WebErrorDecoder(Decoder decoder) {
           this.decoder = decoder;
       }

       @Override
       public Exception decode(String methodKey, Response response) {
           try {
               return (Exception) decoder.decode(response, RuntimeException.class);
           } catch (IOException fallbackToDefault) {
               return defaultDecoder.decode(methodKey, response);
           }
       }
   }
}