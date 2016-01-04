package at.jku.esh.fishbone.web.aktionsfinder;

import java.util.List;

import at.jku.esh.fishbone.web.WebClientBuilder.WebClient;
import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;


/**
 * TODO rename everything
 * interface must NOT be implemented
 *
 */
public interface AktionsFinderClient extends WebClient{
	
	
	  @RequestLine("GET /url/{param1}/{param2}")
	  List<String> getSomething(@Param("param1") String owner, @Param("param2") String repo);
	
	  @RequestLine("POST /url/{param1}")
	  @Headers("Content-Type: application/json")
	  @Body("%7B\"param_1\": \"{param1}\", \"param2\": \"{param2}\"%7D")
	  void postSomeJson(@Param("param1") String param1, @Param("param2") String param2);

}
