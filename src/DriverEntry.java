import java.util.List;
import java.util.ArrayList;
public class DriverEntry implements Comparable<DriverEntry> {
	//carNumber:司机车牌号，代表一位司机
	private String carNumber;
	//upPoints：所有上车点构成的集合，每个点由经纬度表示
	private List<List<Double>>upPoints;
	//downPoints: 所有下车点构成的结合，每个点由经纬度表示
	//上下车点一一对应
	private List<List<Double>>downPoints;
	//netIncome: 司机净收入
	private double netIncome;
	
	public DriverEntry(){
		upPoints=new ArrayList<List<Double>>();
		downPoints=new ArrayList<List<Double>>();
		netIncome=0;
	}
	/**
	 * @return the carNumber
	 */
	public String getCarNumber() {
		return carNumber;
	}
	/**
	 * @return the upPoints
	 */
	public List<List<Double>> getUpPoints() {
		return upPoints;
	}
	/**
	 * @return the downPoints
	 */
	public List<List<Double>> getDownPoints() {
		return downPoints;
	}
	/**
	 * @return the netIncome
	 */
	public double getNetIncome() {
		return netIncome;
	}
	/**
	 * @param carNumber the carNumber to set
	 */
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	/**
	 * @param upPoints the upPoints to set
	 */
	public void setUpPoints(List<List<Double>> upPoints) {
		this.upPoints = upPoints;
	}
	/**
	 * @param downPoints the downPoints to set
	 */
	public void setDownPoints(List<List<Double>> downPoints) {
		this.downPoints = downPoints;
	}
	/**
	 * @param netIncome the netIncome to set
	 */
	public void setNetIncome(double netIncome) {
		this.netIncome = netIncome;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((carNumber == null) ? 0 : carNumber.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DriverEntry other = (DriverEntry) obj;
		if (carNumber == null) {
			if (other.carNumber != null)
				return false;
		} else if (!carNumber.equals(other.carNumber))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	//按照收入降序排序
	@Override
	public int compareTo(DriverEntry o) {
		// TODO Auto-generated method stub
		if(o.netIncome-this.netIncome>0)
			return 1;
		else
			return -1;
	}
}
