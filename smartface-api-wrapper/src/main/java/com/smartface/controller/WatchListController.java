package com.smartface.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.smartface.application.SmartfaceProperties;
import com.smartface.dto.CreateWatchListDTO;
import com.smartface.dto.CreateWatchListMemberDTO;
import com.smartface.dto.DeleteWatchListMemberDto;
import com.smartface.response.ApiResponse;
import com.smartface.service.WatchListMemberService;

import jakarta.validation.Valid;

@RequestMapping("/watchlist")
@RestController
public class WatchListController {

	@Autowired
	SmartfaceProperties smartfaceProperties;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	WatchListMemberService watchListMemberService;

	@GetMapping("/fetch")
	public ResponseEntity<?> fetchWatchLists() {
		try {

			String url = smartfaceProperties.getBaseurl()
					+ "Watchlists?Ascending=true&ShowTotalCount=true&PageNumber=1&PageSize="
					+ smartfaceProperties.getDefaultPageSize();

			Object response = restTemplate.getForObject(url, Object.class);

			if (response == null) {
				ApiResponse<?> apiResponse = new ApiResponse<>("non content available", null,
						HttpStatus.NO_CONTENT.value());
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
			}

			ApiResponse<?> apiResponse = new ApiResponse<>("success", response, HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
		} catch (HttpClientErrorException e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Client error: " + e.getMessage(), null,
					e.getStatusCode().value());
			return ResponseEntity.status(e.getStatusCode()).body(apiResponse);

		} catch (HttpServerErrorException e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Server error: " + e.getResponseBodyAsString(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

		} catch (Exception e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Unexpected error: " + e.getMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
		}

	}

	@PostMapping("/addmember")
	public ResponseEntity<?> addWatchListMember(@RequestBody CreateWatchListMemberDTO createWatchListMemberDTO) {

		String id = null;
		boolean isLinked = false;
		try {
			try {

				id = watchListMemberService.getWatchListMemberId(createWatchListMemberDTO);
//				System.out.println(id);
				createWatchListMemberDTO.setMemberId(id);
			} catch (Exception e) {
				ApiResponse<?> response = new ApiResponse<>("failed to create watchlist member", e,
						HttpStatus.BAD_REQUEST.value());
				return ResponseEntity.badRequest().body(response);
			}

			try {

				isLinked = watchListMemberService.linkWatchListMemberToWatchList(createWatchListMemberDTO.getWatchlistId(),
						createWatchListMemberDTO.getMemberId());
			} catch (Exception e) {
				try {
					watchListMemberService.deleteWatchListMemberById(createWatchListMemberDTO.getMemberId());
				} catch (Exception ex) {
					ApiResponse response = new ApiResponse<>("failed to delete the watchlist member", ex,
							HttpStatus.BAD_REQUEST.value());
					return ResponseEntity.badRequest().body(response);
				}
				ApiResponse response = new ApiResponse<>("failed to link to the watchlist", null,
						HttpStatus.BAD_REQUEST.value());
				return ResponseEntity.badRequest().body(response);
			}

			if (createWatchListMemberDTO.getImageBase64().contains(",")) {
				createWatchListMemberDTO.setImageBase64(createWatchListMemberDTO.getImageBase64()
						.substring(createWatchListMemberDTO.getImageBase64().indexOf(",") + 1));
			}

			try {

				ApiResponse response = watchListMemberService.addImageToWatchListMember(createWatchListMemberDTO);
				return ResponseEntity.status(response.getStatusCode()).body(response);
			} catch (Exception e) {

				try {
					boolean unlinkstatus = watchListMemberService.unLinkWatchListMemberToWatchList(
							createWatchListMemberDTO.getWatchlistId(), createWatchListMemberDTO.getMemberId());
				} catch (Exception ee) {
					ApiResponse unlinkresponse = new ApiResponse<>("failed to unlink to the watchlist", null,
							HttpStatus.BAD_REQUEST.value());
					return ResponseEntity.badRequest().body(unlinkresponse);
				}

				try {
					watchListMemberService.deleteWatchListMemberById(createWatchListMemberDTO.getMemberId());
				} catch (Exception eee) {
					ApiResponse failresponse = new ApiResponse<>("failed to delete the watchlist member", eee,
							HttpStatus.BAD_REQUEST.value());
					return ResponseEntity.badRequest().body(failresponse);
				}
			}

			ApiResponse failresponse = new ApiResponse<>("failure", null, HttpStatus.BAD_REQUEST.value());
			return ResponseEntity.badRequest().body(failresponse);

		} catch (Exception e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Error while adding member: " + e.getMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
		}
	}

	@PostMapping("/create")
	public ResponseEntity<?> createWatchList(@Valid @RequestBody CreateWatchListDTO createWatchListDTO,
			BindingResult result) {
		try {
			if (!result.hasErrors()) {

				String url = smartfaceProperties.getBaseurl() + "Watchlists";

				ResponseEntity<?> response = restTemplate.postForEntity(url, createWatchListDTO, Object.class);

				ApiResponse<?> apiResponse = new ApiResponse<>("Watchlist was created successfully", response.getBody(),
						response.getStatusCodeValue());
				return ResponseEntity.status(response.getStatusCode()).body(apiResponse);
			} else {
				List<String> errorMessages = result.getAllErrors().stream().map(error -> error.getDefaultMessage())
						.collect(Collectors.toList());

				ApiResponse<?> response = new ApiResponse("error creating watchlist", errorMessages,
						HttpStatus.BAD_REQUEST.value());
				return ResponseEntity.badRequest().body(response);
			}

		} catch (HttpClientErrorException e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Client error", e.getResponseBodyAsString(),
					e.getStatusCode().value());
			return ResponseEntity.status(e.getStatusCode()).body(apiResponse);

		} catch (HttpServerErrorException e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Server error", e.getResponseBodyAsString(),
					e.getStatusCode().value());
			return ResponseEntity.status(e.getStatusCode()).body(apiResponse);

		} catch (Exception e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Unexpected error: " + e.getMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
		}

	}

	@PostMapping("/delete")
	public ResponseEntity<?> deleteWatchList(@RequestBody Map<String, String> request) {
		try {
			String watchlistId = request.get("id");
			if (watchlistId == null || watchlistId.isEmpty()) {
				return ResponseEntity.badRequest()
						.body(new ApiResponse<>("Watchlist ID is required", null, HttpStatus.BAD_REQUEST.value()));
			}

			String url = smartfaceProperties.getBaseurl() + "Watchlists/" + watchlistId;

			Object response = restTemplate.exchange(url,HttpMethod.DELETE,null,Object.class);
			if(response == null) {
				ApiResponse<?> apiResponse = new ApiResponse<>("No Content found",null,HttpStatus.NO_CONTENT.value());
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
						}
			ApiResponse<String> apiResponse = new ApiResponse<>("Watchlist deleted successfully", null,
					HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

		} catch (HttpClientErrorException e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Client error", e.getResponseBodyAsString(),
					e.getStatusCode().value());
			return ResponseEntity.status(e.getStatusCode()).body(apiResponse);

		} catch (HttpServerErrorException e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Server error", e.getResponseBodyAsString(),
					e.getStatusCode().value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

		} catch (Exception e) {
			ApiResponse<String> apiResponse = new ApiResponse<>("Unexpected error: " + e.getMessage(), null,
					HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
		}
	}
	

	

}
