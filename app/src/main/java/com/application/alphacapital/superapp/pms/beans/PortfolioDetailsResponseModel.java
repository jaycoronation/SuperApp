package com.application.alphacapital.superapp.pms.beans;

public class PortfolioDetailsResponseModel{
	private PortfolioDetails portfolioDetails;
	private int success;

	public void setPortfolioDetails(PortfolioDetails portfolioDetails){
		this.portfolioDetails = portfolioDetails;
	}

	public PortfolioDetails getPortfolioDetails(){
		return portfolioDetails;
	}

	public void setSuccess(int success){
		this.success = success;
	}

	public int getSuccess(){
		return success;
	}


	public static class PortfolioDetails{
		private String Nominee;
		private String RemainingUnits;
		private String Joint2Name;
		private String DPID;
		private String CurrentValue;
		private String SchemeName;
		private String Joint1Name;
		private String Joint2PANv;
		private String Joint1PAN;

		public void setNominee(String nominee){
			this.Nominee = nominee;
		}

		public String getNominee(){
			return Nominee;
		}

		public void setRemainingUnits(String remainingUnits){
			this.RemainingUnits = remainingUnits;
		}

		public String getRemainingUnits(){
			return RemainingUnits;
		}

		public void setJoint2Name(String joint2Name){
			this.Joint2Name = joint2Name;
		}

		public String getJoint2Name(){
			return Joint2Name;
		}

		public void setDPID(String dPID){
			this.DPID = dPID;
		}

		public String getDPID(){
			return DPID;
		}

		public void setCurrentValue(String currentValue){
			this.CurrentValue = currentValue;
		}

		public String getCurrentValue(){
			return CurrentValue;
		}

		public void setSchemeName(String schemeName){
			this.SchemeName = schemeName;
		}

		public String getSchemeName(){
			return SchemeName;
		}

		public void setJoint1Name(String joint1Name){
			this.Joint1Name = joint1Name;
		}

		public String getJoint1Name(){
			return Joint1Name;
		}

		public void setJoint2PANv(String joint2PANv){
			this.Joint2PANv = joint2PANv;
		}

		public String getJoint2PANv(){
			return Joint2PANv;
		}

		public void setJoint1PAN(String joint1PAN){
			this.Joint1PAN = joint1PAN;
		}

		public String getJoint1PAN(){
			return Joint1PAN;
		}
	}

}
