package com.hyunn.capstone.dto.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

// 유저에게 요청 받는 dto
@Getter
public class ImageRequest {
  @NotBlank(message = "이미지를 입력해주세요.")
  private String image;

  @NotBlank(message = "성별을 입력해주세요.")
  @Min(value = 0, message = "남자는 0, 여자는 1로 입력해주세요.")
  @Max(value = 1, message = "남자는 0, 여자는 1로 입력해주세요.")
  private Boolean gender;

  @NotBlank(message = "감정을 입력해주세요.")
  private String emotion;

  public ImageRequest(String image, Boolean gender, String emotion) {
    this.image = image;
    this.gender = gender;
    this.emotion = emotion;
  }

  public static ImageRequest create(String image, Boolean gender, String emotion) {
    return new ImageRequest(image, gender, emotion);
  }
}