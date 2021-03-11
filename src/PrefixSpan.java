import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
//��ʱ����дget set������������Ϊprivate�����õķ�װ
public class PrefixSpan {
	//topdrivers�������ȡ��ĳ������ȼ�˾���������Ϣ
	private	Set<DriverEntry>topdrivers;
	//seqSet:�������ݿ�S�����ÿ��˾�������¿͵�POI���ͱ��빹�ɵĹ켣
	private List<List<Integer>>seqSet;
	//freqSeq:������е�Ƶ������������
	private List<List<Integer>>freqSeq;
	//poiNames��poi���ͱ��룬��poi���Ͷ�Ӧ�����ڽ�poi���ͱ����poi�����໥ת��
	private HashMap<Integer,String>poiNames;
	//minsup����С֧�ֶ�
	private double minsup;
	//map��HashMap<�������ݿ���ÿ������,Map<poi���ͱ�ţ�������poi�����ڴ������е���Щλ��>>
	private HashMap<Integer,Map<Integer,List<Integer>>>map;
	//seqNum����������
	private int seqNum;
	//minSupportNum����С֧����
	private int minSupportNum;
	//dbo�����ݿ����������Ѱ�����poi��
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
		//ÿ��˾����ÿ��poi�㣬������list����Щλ��
		map=new HashMap<Integer,Map<Integer,List<Integer>>>();
		seqSet=new ArrayList<List<Integer>>();
		poiNames=new HashMap<Integer,String>();
		Iterator<DriverEntry> it=topdrivers.iterator();
		int test=0;
		while(it.hasNext())
		{
			System.out.println("testΪ������ʾ�ڼ������У�"+test++);
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
					//����޷����뵽����һ��forѭ���Ļ������digitΪ"",����Integer.valueOf(digit)�ᱨ��
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
					 //ֻ���ϳ�����о��࣬���޸ģ�����
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
	//��ʵ������Ƶ��һ�������У�Ȼ�󲻶����µݹ�õ����е�Ƶ������������
	public void genFreqSeq()
	{
		freqSeq=new ArrayList<List<Integer>>();
		for(Map.Entry<Integer, String>entry:poiNames.entrySet())
		{
			//����ÿһ��һ�
			int count=0;
			//Map<Sequence,position>
			Map<Integer,List<Integer>> freq=new HashMap<Integer,List<Integer>>();
			for(int i=0;i<seqSet.size();i++)
			{
				if(seqSet.get(i).contains(entry.getKey()))
				{
					count++;
					List<Integer>positions=map.get(i).get(entry.getKey());
					//entry��k�����ڵ�i��seq����Щλ��: ����,λ����
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
		System.out.println("��"+tempSeq.size()+"��ѭ��");
		//Map<��һ��,Map<��һ����������,List<��һ�����ڴ������е�λ��(����Щ)>>>
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
					//next����һ��
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
		//end:���������³���
		//end2: ���������³���
		//end3���������ϳ���
		String sourcePath="D:\\mystudy\\oneday.txt";
		//���޸ģ�����
		String goalPath="D:\\mystudy\\compare\\freqSeq_low1%.txt";
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now=df.format(new Date());
		now=now+"\n";
		TxtWrite.saveAsFileWriter(now,goalPath);
		//���޸ģ�����
		TxtWrite.saveAsFileWriter("minsup=0.01\n", goalPath);
		int topNum=7000;
		int all=100000;
		Set<DriverEntry>topdrivers=FindTop.findTop(sourcePath, all, topNum);
		if(topdrivers!=null)
		{
			//���޸ģ�����
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


