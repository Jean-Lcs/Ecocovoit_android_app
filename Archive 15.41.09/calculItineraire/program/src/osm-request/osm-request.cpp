#include "osm-request.h"

namespace osm_request {

static size_t mcurl_write_funct(void *contents, size_t size, size_t nmemb, std::string *str) {
	str->append((char*) contents, nmemb);
	return nmemb;
}

std::string query_xml_map(std::string osm_request, std::string osm_url) {
	CURL * curl;
	curl = curl_easy_init();
	std::string response;

	if(curl) {
		// Set up the url
		curl_easy_setopt(curl, CURLOPT_URL, osm_url.data());

		// Set up the request
		const char * msg = osm_request.data();
		curl_easy_setopt(curl, CURLOPT_POSTFIELDS, msg);
		curl_easy_setopt(curl, CURLOPT_POSTFIELDSIZE, strlen(msg));

		// Set up the response handling
		curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, mcurl_write_funct);
		curl_easy_setopt(curl, CURLOPT_WRITEDATA, &response);

		CURLcode res = curl_easy_perform(curl);

		if(res != CURLE_OK) {
			std::cerr << "Curl perform fail: " << curl_easy_strerror(res) << std::endl;
			std::cerr << "url = " << osm_url << std::endl;
			return "Curl perform fail";
		}else {
			curl_easy_cleanup(curl);
			return response;
		}

	}
	else {
		std::cerr << "Curl fail" << std::endl;
		return "Curl fail";
	}
}

}