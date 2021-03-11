import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
//有时间重写get set方法，属性置为private，更好的封装
public class PrefixSpan {
	//topdrivers：存放提取的某种收入等级司机的相关信息
	private	Set<DriverEntry>topdrivers;
	//seqSet:序列数据库S，存放每个司机由上下客点POI类型编码构成的轨迹
	private List<List<Integer>>seqSet;
	//freqSeq:存放所有的频繁连续子序列
	private List<List<Integer>>freqSeq;
	//poiNames：poi类型编码，和poi类型对应，便于将poi类型编码和poi名称相互转换
	private HashMap<Integer,String>poiNames;
	//minsup：最小支持度
	private double minsup;
	//map：HashMap<序列数据库中每条序列,Map<poi类型编号，该类型poi出现在此条序列的哪些位置>>
	private HashMap<Integer,Map<Integer,List<Integer>>>map;
	//seqNum：序列总数
	private int seqNum;
	//minSupportNum：最小支持数
	private int minSupportNum;
	//dbo：数据库操作，用于寻找最近poi点
	private	DBOperate dbo=DBOperate.getInstance();
	
	/**
	 * @return the topdrivers
	 */
	public Set<DriverEntry> getTopdrivers() {
		return topdrivers;
	}
	/**
	 * @return the seqSet
	 */
	public List<List<Integer>> getSeqSet() {
		return seqSet;
	}
	/**
	 * @return the freqSeq
	 */
	public List<List<Integer>> getFreqSeq() {
		return freqSeq;
	}
	/**
	 * @return the poiNames
	 */
	public HashMap<Integer, String> getPoiNames() {
		return poiNames;
	}
	/**
	 * @return the minsup
	 */
	public double getMinsup() {
		return minsup;
	}
	/**
	 * @return the map
	 */
	public HashMap<Integer, Map<Integer, List<Integer>>> getMap() {
		return map;
	}
	/**
	 * @return the seqNum
	 */
	public int getSeqNum() {
		return seqNum;
	}
	/**
	 * @return the minSupportNum
	 */
	public int getMinSupportNum() {
		return minSupportNum;
	}
	/**
	 * @param topdrivers the topdrivers to set
	 */
	public void setTopdrivers(Set<DriverEntry> topdrivers) {
		this.topdrivers = topdrivers;
	}
	/**
	 * @param seqSet the seqSet to set
	 */
	public void setSeqSet(List<List<Integer>> seqSet) {
		this.seqSet = seqSet;
	}
	/**
	 * @param freqSeq the freqSeq to set
	 */
	public void setFreqSeq(List<List<Integer>> freqSeq) {
		this.freqSeq = freqSeq;
	}
	/**
	 * @param poiNames the poiNames to set
	 */
	public void setPoiNames(HashMap<Integer, String> poiNames) {
		this.poiNames = poiNames;
	}
	/**
	 * @param minsup the minsup to set
	 */
	public void setMinsup(double minsup) {
		this.minsup = minsup;
	}
	/**
	 * @param map the map to set
	 */
	public void setMap(HashMap<Integer, Map<Integer, List<Integer>>> map) {
		this.map = map;
	}
	/**
	 * @param seqNum the seqNum to set
	 */
	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}
	/**
	 * @param minSupportNum the minSupportNum to set
	 */
	public void setMinSupportNum(int minSupportNum) {
		this.minSupportNum = minSupportNum;
	}
	
	public PrefixSpan(double minsup,Set<DriverEntry>topdrivers)
	{
		this.minsup=minsup;
		this.topdrivers=topdrivers;
	}
	public void genSeqset()
	{
		//每个司机的每个poi点，出现在list的哪些位置
		map=new HashMap<Integer,Map<Integer,List<Integer>>>();
		seqSet=new ArrayList<List<Integer>>();
		poiNames=new HashMap<Integer,String>();
		Iterator<DriverEntry> it=topdrivers.iterator();
		int test=0;
		while(it.hasNext())
		{
			System.out.println("test为：（表示第几条序列）"+test++);
			List<Integer>updownLine=new ArrayList<>();
			Map<Integer,List<Integer>>tempM=new HashMap<Integer,List<Integer>>();
			DriverEntry driver=it.next();
			List<List<Double>> upPoints=driver.getUpPoints();
			List<List<Double>> downPoints=driver.getDownPoints();
			for(int i=0;i<upPoints.size()&&i<downPoints.size();i++)
			{
				try {
					String upCLA=dbo.executeQuery_LuckyPoint(upPoints.get(i).get(0), upPoints.get(i).get(1));	
					char[]upCLAarray=upCLA.toCharArray();
					String digit="";
					String poiName="";
					for(int j=0;j<upCLA.length();j++)
					{
						if(upCLAarray[j]==']')
							break;
						while((int)upCLAarray[j]>=48&&(int)upCLAarray[j]<=57)
						{
							digit=digit+upCLAarray[j];
							j++;
						}			
						if(upCLAarray[j]!='['&&upCLAarray[j]!=',')
						{
							poiName=poiName+upCLAarray[j];
						}
					}
					//如果无法进入到上面一个for循环的话，这边digit为"",调用Integer.valueOf(digit)会报错
					if(digit!="")
					{	
						int idigit=Integer.valueOf(digit);
						poiNames.put(idigit, poiName);
						updownLine.add(idigit);
						if(tempM.containsKey(idigit))
						{
							tempM.get(idigit).add(updownLine.size()-1);
						}
						else {
							List<Integer>position=new ArrayList<>();
							position.add(updownLine.size()-1);
							tempM.put(idigit, position);
						}
					}
					 //只对上车点进行聚类，需修改！！！
					String downCLA=dbo.executeQuery_LuckyPoint(downPoints.get(i).get(0), downPoints.get(i).get(1));
					char[]downCLAarray=downCLA.toCharArray();
					digit="";
					poiName="";
					for(int j=0;j<downCLA.length();j++)
					{
						if(downCLAarray[j]==']')
							break;
						while((int)downCLAarray[j]>=48&&(int)downCLAarray[j]<=57)
						{
							digit=digit+downCLAarray[j];
							j++;
						}			
						if(downCLAarray[j]!='['&&downCLAarray[j]!=',')
						{
							poiName=poiName+downCLAarray[j];
						}
					}
					if(digit!="")
					{
						int idigit=Integer.valueOf(digit);
						poiNames.put(idigit, poiName);
						updownLine.add(idigit);
						if(tempM.containsKey(idigit))
						{
							tempM.get(idigit).add(updownLine.size()-1);
						}
						else {
							List<Integer>position=new ArrayList<>();
							position.add(updownLine.size()-1);
							tempM.put(idigit, position);
						}
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			seqSet.add(updownLine);
			map.put(seqSet.size()-1, tempM);
		}
		seqNum=seqSet.size();
		minSupportNum=(int)(seqNum*minsup);
	}
	//其实是生成频繁一项子序列，然后不断向下递归得到所有的频繁连续子序列
	public void genFreqSeq()
	{
		freqSeq=new ArrayList<List<Integer>>();
		for(Map.Entry<Integer, String>entry:poiNames.entrySet())
		{
			//对于每一个一项集
			int count=0;
			//Map<Sequence,position>
			Map<Integer,List<Integer>> freq=new HashMap<Integer,List<Integer>>();
			for(int i=0;i<seqSet.size();i++)
			{
				if(seqSet.get(i).contains(entry.getKey()))
				{
					count++;
					List<Integer>positions=map.get(i).get(entry.getKey());
					//entry的k出现在第i个seq的哪些位置: 序列,位置们
					freq.put(i, positions);
				}
			}
			if(count>minSupportNum)
			{
				List<Integer> freqItems=new ArrayList<>();
				freqItems.add(entry.getKey());
				freqSeq.add(freqItems);
				genFreqSeq_n(freqItems,freq);
			}
		}
	}
	private void genFreqSeq_n(List<Integer>tempSeq,Map<Integer,List<Integer>>tempsFreq)
	{
		System.out.println("第"+tempSeq.size()+"重循环");
		//Map<下一个,Map<下一个所在序列,List<下一个所在此序列中的位置(有哪些)>>>
		Map<Integer,Map<Integer,List<Integer>>> nextC=new HashMap<Integer,Map<Integer,List<Integer>>>();
		for(Map.Entry<Integer, List<Integer>> entry:tempsFreq.entrySet())
		{
			int seq=entry.getKey();
			List<Integer>pos=entry.getValue();
			for(int i=0;i<pos.size();i++)
			{
				int p=pos.get(i);
				if(p<seqSet.get(seq).size()-1)
				{	
					//next是下一个
					int next=seqSet.get(seq).get(p+1);
					if(nextC.containsKey(next))
					{
						Map<Integer,List<Integer>> positions=nextC.get(next);
						if(positions.containsKey(seq))
						{
							positions.get(seq).add(p+1);
						}
						else 
						{
							List<Integer> temp=new ArrayList<>();
							temp.add(p+1);
							positions.put(seq, temp);
						}
					}
					else
					{
						List<Integer>temp=new ArrayList<>();
						temp.add(p+1);
						Map<Integer,List<Integer>> stemp=new HashMap<Integer,List<Integer>>();
						stemp.put(seq,temp);
						nextC.put(next, stemp);
					}
				}
			}
		}
		for(Map.Entry<Integer,Map<Integer,List<Integer>>> entry:nextC.entrySet())
		{
			Integer next=entry.getKey();
			Map<Integer,List<Integer>> nextseqs=entry.getValue();
			if(nextseqs.size()>minSupportNum)
			{
				List<Integer>freqItems=new ArrayList<>();
				freqItems.addAll(tempSeq);
				freqItems.add(next);
				freqSeq.add(freqItems);
				genFreqSeq_n(freqItems,nextseqs);
			}
		}
	}
	
	public static void main(String[] args)
	{
		//end:高收入上下车点
		//end2: 低收入上下车点
		//end3：高收入上车点
		String sourcePath="D:\\mystudy\\oneday.txt";
		//需修改！！！
		String goalPath="D:\\mystudy\\compare\\freqSeq_low1%.txt";
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now=df.format(new Date());
		now=now+"\n";
		TxtWrite.saveAsFileWriter(now,goalPath);
		//需修改！！！
		TxtWrite.saveAsFileWriter("minsup=0.01\n", goalPath);
		int topNum=7000;
		int all=100000;
		Set<DriverEntry>topdrivers=FindTop.findTop(sourcePath, all, topNum);
		if(topdrivers!=null)
		{
			//需修改！！！
			PrefixSpan pf=new PrefixSpan(0.01,topdrivers);
			pf.genSeqset();
			pf.genFreqSeq();
			List<List<Integer>> freqSeq=pf.getFreqSeq();
			HashMap<Integer,String>poiNames=pf.getPoiNames();
			for(int i=0;i<freqSeq.size();i++)
			{
				String one_freq="<";
				List<Integer>trace=freqSeq.get(i);
				for(int j=0;j<trace.size();j++)
				{
					int poi_number=trace.get(j);
					String poiname=poiNames.get(poi_number);
					one_freq=one_freq+poiname+" ";
				}
				one_freq=one_freq+">\n";
				TxtWrite.saveAsFileWriter(one_freq,goalPath);
			}
		}
		now=df.format(new Date());
		now=now+"\n";
		TxtWrite.saveAsFileWriter(now,goalPath);
	}
}


