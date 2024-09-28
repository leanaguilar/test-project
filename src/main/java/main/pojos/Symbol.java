package main.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

//Class to map the symbol properties
public class Symbol {

	@JsonProperty("reward_multiplier")
	private Double rewardMultiplier;
	private String type;
	private String impact; // Optional (only for bonus symbols)
	private Integer extra; // Optional (only for bonus symbols with extra)

	// Getters and Setters
	public Double getRewardMultiplier() {
		return rewardMultiplier;
	}

	public void setRewardMultiplier(Double rewardMultiplier) {
		this.rewardMultiplier = rewardMultiplier;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public Integer getExtra() {
		return extra;
	}

	public void setExtra(Integer extra) {
		this.extra = extra;
	}
}