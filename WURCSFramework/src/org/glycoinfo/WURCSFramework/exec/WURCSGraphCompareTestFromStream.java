package org.glycoinfo.WURCSFramework.exec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.array.WURCSExporter;
import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSArrayToGraph;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSGraphToArray;
import org.glycoinfo.WURCSFramework.util.graph.WURCSGraphNormalizer;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
//muller for debug 180531
import org.glycoinfo.WURCSFramework.wurcs.graph.Backbone;
import org.glycoinfo.WURCSFramework.wurcs.graph.LinkagePosition;
import org.glycoinfo.WURCSFramework.wurcs.graph.Modification;
import org.glycoinfo.WURCSFramework.wurcs.graph.ModificationAlternative;
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSEdge;
// end muller
import org.glycoinfo.WURCSFramework.wurcs.graph.WURCSGraph;

public class WURCSGraphCompareTestFromStream {

	public static void main(String[] args) {

//		String t_strDir = "/Users/muller/up_test/wurcsframework/WURCSFramework/test/org/glycoinfo/WURCSFramework/test2/";
//		String t_strWURCSFile = "test_stream_compare.txt";
//		String t_strWURCSFile = "graph_test.txt";
//		String input  = t_strDir + t_strWURCSFile;
//		String output = t_strDir + "test_stream_compare_out.txt";
//		String output = t_strDir + "graph_test_out.txt";
//		String output = t_strDir + "test_stream_compare_graph_out.txt";
		int i;
		boolean eq;

		TreeMap<String, LinkedList<String>> t_mapWURCSIndex = new TreeMap<String, LinkedList<String>>();
		WURCSImporter t_objImporter = new WURCSImporter();

//		if(! new File(input).isFile() ) {
//			System.err.println("File not found");
//			System.exit(0);
//		}

		try {
			t_mapWURCSIndex = readWURCS2(new BufferedReader(new InputStreamReader(System.in,"utf-8")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			WURCSExporter t_oExporter = new WURCSExporter();
			HashMap<String,String> inWURCS=new HashMap<String,String>();
			HashMap<String,String> outWURCS =new HashMap<String,String>();
			HashMap<String,LinkedList<Backbone>> inBacks=new HashMap<String,LinkedList<Backbone>>();
			HashMap<String,LinkedList<Backbone>> outBacks=new HashMap<String,LinkedList<Backbone>>();
			HashMap<String,LinkedList<Modification>> inMods=new HashMap<String,LinkedList<Modification>>();
			HashMap<String,LinkedList<Modification>> outMods=new HashMap<String,LinkedList<Modification>>();
			HashMap<String,LinkedList<ModificationAlternative>> inModAlts=new HashMap<String,LinkedList<ModificationAlternative>>();
			HashMap<String,LinkedList<ModificationAlternative>> outModAlts=new HashMap<String,LinkedList<ModificationAlternative>>();
// standardization of WURCS for all keys in input files
			for(String key : t_mapWURCSIndex.keySet()) {
				System.err.println(key+":");
//standardization of two WURCS ( in and out )
				i=0;
				for(String t_strOrigWURCS: t_mapWURCSIndex.get(key)) {
					System.err.println("in: "+t_strOrigWURCS);
					WURCSArray t_oWURCS = t_objImporter.extractWURCSArray(t_strOrigWURCS);
					WURCSArrayToGraph t_oA2G = new WURCSArrayToGraph();
					t_oA2G.start(t_oWURCS);
					WURCSGraph t_oGraph = t_oA2G.getGraph();
					WURCSGraphNormalizer t_oNorm = new WURCSGraphNormalizer();
					t_oNorm.start(t_oGraph);
					WURCSGraphToArray t_oG2A = new WURCSGraphToArray();
					t_oG2A.start(t_oGraph);
// here the graph is standardized
// i==0 means inputed data in "matsubara test" such as inWURCS or inGraph
// i==1 means outputed data in "matsubara test" such as outWURCS or outGraph
					if(i==0) inWURCS.put(key,t_oExporter.getWURCSString(t_oG2A.getWURCSArray()));
					if(i==1) outWURCS.put(key,t_oExporter.getWURCSString(t_oG2A.getWURCSArray()));
					if(i==0) inBacks.put(key,t_oG2A.getG2ABackbones());
					if(i==1) outBacks.put(key,t_oG2A.getG2ABackbones());
					if(i==0) inMods.put(key,t_oG2A.getG2AModifications());
					if(i==1) outMods.put(key,t_oG2A.getG2AModifications());
					if(i==0) inModAlts.put(key,t_oG2A.getG2AModificationAlternatives());
					if(i==1) outModAlts.put(key,t_oG2A.getG2AModificationAlternatives());
					i++;
				}
	            eq=compareTwoGraphs(inBacks.get(key), inMods.get(key), inModAlts.get(key), outBacks.get(key), outMods.get(key), outModAlts.get(key));
				System.out.print(key+": ");
				if(!eq) {
					System.out.print(inWURCS.get(key)+"\n"+"          "+outWURCS.get(key));
					}
				System.out.print("\n");
			}
		System.out.close();
		} catch (WURCSFormatException e) {
			e.printStackTrace();
		} catch (WURCSException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String join(Set<String> t_aList ) {
		String t_strJoin = "";
		int count = 0;
		for ( String t_strInv : t_aList ) {
			if ( !t_strJoin.equals("") ) t_strJoin += ",";
			if ( count != 0 && count % 20 == 0 ) t_strJoin += "\n";
			t_strJoin += t_strInv;
			count++;
		}
		return t_strJoin;
	}

	/** Open text file for write */
	public static PrintWriter openTextFileW( String fileName ) throws Exception {
		String charSet = "utf-8";
		boolean append = false;
		boolean autoFlush = true;
		return new PrintWriter( new BufferedWriter( new OutputStreamWriter( new FileOutputStream( new File(fileName), append ), charSet ) ), autoFlush );

	}

	//input WURCS string file
	public static TreeMap<String, LinkedList<String>> openString2(String a_strFile) throws Exception {
		try {
			return readWURCS2(new BufferedReader(new FileReader(a_strFile)));
		}catch (IOException e) {
			throw new Exception();
		}
	}

	public static TreeMap<String, LinkedList<String>> readWURCS2(BufferedReader a_bfFile) throws IOException {
		String line = "";
		TreeMap<String, LinkedList<String>> wret = new TreeMap<String, LinkedList<String>>();
//		LinkedList<String> wurcslist = new LinkedList<String>();
		ArrayList<LinkedList<String>> wurcslist = new ArrayList<LinkedList<String>>();
		wret.clear();
		wurcslist.clear();
		int i=0;
		while((line = a_bfFile.readLine()) != null) {
			line.trim();
			if(line.indexOf("WURCS") != -1) { 						// if not blank line
				String[] IDandWURCS  = line.split("\t");  			// split by tab
				if (IDandWURCS.length == 3) {						// 	key and WURCS ( 2 element )
					wurcslist.add(i,new LinkedList<String>());
					wurcslist.get(i).add(IDandWURCS[1].trim());
					wurcslist.get(i).add(IDandWURCS[2].trim());
					wret.put(IDandWURCS[0].trim(), wurcslist.get(i));	// add to tree map "wret"
					}
				}
			i++;
			}
// debug 
//		for(String key : wret.keySet()) {
//			System.out.print("readed WURCS: "+ key);
//			for(String wurcss : wret.get(key)) {
//				System.out.print(" : "+wurcss );
//				}
//			System.out.print("\n");
//			}
//		System.out.print("\n");
// end of debug
		a_bfFile.close();
		return wret;
	}
	public static boolean compareTwoBackbones(LinkedList<Backbone> b1, LinkedList<Backbone> b2) {
		boolean equiv=false;
		System.err.println("in_compareTwoBackbones: ");
		int sizeOfBackbone1=b1.size();
		int sizeOfBackbone2=b2.size();
		System.err.println("size of backbone: "+sizeOfBackbone1+","+sizeOfBackbone2);
		if(sizeOfBackbone1!=sizeOfBackbone2) return equiv;
//
		Backbone bk1;
		Backbone bk2;
		for(int i=0;i<sizeOfBackbone1;i++) {
			bk1=b1.get(i);
			bk2=b1.get(i);
			System.err.print(i+"skeleton:"+bk1.getSkeletonCode()+" "+bk2.getSkeletonCode());
			if(bk1.getSkeletonCode().compareTo(bk2.getSkeletonCode())!=0) return equiv;
			int sizeOfBackboneEdge1=bk1.getEdges().size();
			int sizeOfBackboneEdge2=bk2.getEdges().size();
			System.err.print(" edge size:"+sizeOfBackboneEdge1+" "+sizeOfBackboneEdge2);
			if(sizeOfBackboneEdge1!=sizeOfBackboneEdge2) return equiv;
			for(int j=0;j<sizeOfBackboneEdge1;j++) {
				WURCSEdge bk1ed=bk1.getEdges().get(j);
				WURCSEdge bk2ed=bk1.getEdges().get(j);
				if(bk1ed.getModification().isGlycosidic()!=bk2ed.getModification().isGlycosidic()) return equiv;
				}
			for(int j=0;j<sizeOfBackboneEdge1;j++) {
				WURCSEdge bk1ed=bk1.getEdges().get(j);
				WURCSEdge bk2ed=bk1.getEdges().get(j);
				if(! bk1ed.getModification().isGlycosidic()) {
					System.err.print(" modification"+bk1ed.getModification().getMAPCode()+" "+bk2ed.getModification().getMAPCode());
					if(bk1ed.getModification().getMAPCode().compareTo(bk2ed.getModification().getMAPCode())!=0) return equiv;
					}
				int sizeOfBackboneEdgeLink1=bk1ed.getLinkages().size();
				int sizeOfBackboneEdgeLink2=bk2ed.getLinkages().size();
				if(sizeOfBackboneEdgeLink1!=sizeOfBackboneEdgeLink2) return equiv;
				for(int k=0;k<sizeOfBackboneEdgeLink1;k++) {
					LinkagePosition bk1edlk=bk1ed.getLinkages().get(k);
					LinkagePosition bk2edlk=bk2ed.getLinkages().get(k);
					System.err.print(" bk_linkage: "+bk1edlk.getBackbonePosition()+" "+bk2edlk.getBackbonePosition());
					if(bk1edlk.getBackbonePosition()!=bk2edlk.getBackbonePosition()) return equiv;
					}
				}
			System.err.print("\n");
			}
		equiv=true;
		return equiv;
		}

	public static boolean compareTwoGraphs(LinkedList<Backbone> b1, LinkedList<Modification> m1, LinkedList<ModificationAlternative> ma1, LinkedList<Backbone> b2, LinkedList<Modification> m2, LinkedList<ModificationAlternative> ma2) {
//	public static boolean compareTwoGraphs(ArrayList<Backbone> b1, ArrayList<Backbone> b2) {
		boolean equiv=false;
		System.err.println("in_compareTwoBackbones: ");
		int sizeOfBackbone1=b1.size();
		int sizeOfBackbone2=b2.size();
		System.err.println("size of backbone: "+sizeOfBackbone1+","+sizeOfBackbone2);
		if(sizeOfBackbone1!=sizeOfBackbone2) {System.err.print("size of Backbone");return equiv;}
//
		Backbone bk1;
		Backbone bk2;
// loop of comparing the backbones
		for(int i=0;i<sizeOfBackbone1;i++) {
			bk1=b1.get(i);
			bk2=b2.get(i);
			System.err.print(i+"skeleton:"+bk1.getSkeletonCode()+" "+bk1.getAnomericPosition()+bk1.getAnomericSymbol());
			System.err.print(" "          +bk2.getSkeletonCode()+" "+bk2.getAnomericPosition()+bk2.getAnomericSymbol());
			if(bk1.getSkeletonCode().compareTo(bk2.getSkeletonCode())!=0) {System.err.println("\ndifferent SkeletonCode");return equiv;}
			if(bk1.getAnomericPosition()!=bk2.getAnomericPosition()) {System.err.println("\ndifferent AnomericPosition");return equiv;}
			if(bk1.getAnomericSymbol()!=bk2.getAnomericSymbol()) {System.err.println("\ndiffernt Anomeric Symbol");return equiv;}
//	
// compare the edges for each backbone
			int sizeOfBackboneEdge1=bk1.getEdges().size();
			int sizeOfBackboneEdge2=bk2.getEdges().size();
			System.err.print(" edge size:"+sizeOfBackboneEdge1+" "+sizeOfBackboneEdge2);
			if(sizeOfBackboneEdge1!=sizeOfBackboneEdge2) {System.err.print("\ndifferent size of Edges");return equiv;}
			for(int j=0;j<sizeOfBackboneEdge1;j++) {
				WURCSEdge bk1ed=bk1.getEdges().get(j);
				WURCSEdge bk2ed=bk1.getEdges().get(j);
				System.err.print("\nedges"+j+":|"+bk1ed.getModification().getMAPCode()+"_"+bk1ed.getBackbone().getSkeletonCode()+"|"+bk2ed.getModification().getMAPCode()+"_"+bk2ed.getBackbone().getSkeletonCode());
				System.err.print("|reverse "+bk1ed.isReverse()+" "+bk2ed.isReverse()+"| anomeric"+bk1ed.isAnomeric()+" "+bk2ed.isAnomeric());
				if(bk1ed.isReverse() !=  bk2ed.isReverse()) {System.err.print("\ndifferent reverse flag");return equiv;}
				if(bk1ed.isAnomeric() !=  bk2ed.isAnomeric()) {System.err.print("\ndifferent Anomeric flag");return equiv;}
				if(bk1ed.getModification().getMAPCode().compareTo(bk2ed.getModification().getMAPCode())!=0) {System.err.print("\ndiffernet Edge Modification MAPCode");return equiv;}
				if(bk1ed.getBackbone().getSkeletonCode().compareTo(bk2ed.getBackbone().getSkeletonCode())!=0) {System.err.print("\ndiffernt Edge Backbone Skeleton");return equiv;}
				System.err.print("|isGlycosidic: "+bk1ed.getModification().isGlycosidic()+" "+bk2ed.getModification().isGlycosidic());
				if(bk1ed.getModification().isGlycosidic() != bk2ed.getModification().isGlycosidic()){System.err.print("\ndiffernt Modification glycosidic flag");return equiv;}
// compare glycosidic linkage
//				if(bk1ed.getModification().isGlycosidic() == true) {
//					int mlsize1=bk1ed.getModification().getEdges().size();
//					int mlsize2=bk2ed.getModification().getEdges().size();
//					System.err.print("\n|glycosidicEdge: size: "+mlsize1+" "+mlsize2);
//					if(mlsize1!=mlsize2) {System.err.print("different glycosidic edge size"); return equiv;}
//					for(int jj=0;j<=mlsize1;jj++) {
//						WURCSEdge bk1ge = bk1ed.getModification().getEdges().get(jj);
//						WURCSEdge bk2ge = bk2ed.getModification().getEdges().get(jj);
//						for(int ii=0;ii<b1.size();ii++) {
//							int sizeOfLink1=bk1ge.getLinkages().size();
//							int sizeOfLink2=bk2ge.getLinkages().size();
//							if(sizeOfLink1 != sizeOfLink2) {System.err.print("differnet number of linkages"); return equiv;}
//							for(int iii=0;iii<sizeOfLink1;iii++) {
//								System.err.print("|backbonePosition:"+bk1ge.getLinkages().get(iii).getBackbonePosition()+" "+bk2ge.getLinkages().get(iii).getBackbonePosition());
//								}
//							}
//						}
//					}
//
				System.err.print("|isAglycone: "+bk1ed.getModification().isAglycone()+" "+bk2ed.getModification().isAglycone());
//				if((bk1ed.getModification().isAglycone() == true && bk2ed.getModification().isAglycone() == false) || (bk1ed.getModification().isAglycone() == false && bk2ed.getModification().isAglycone() == true)) {System.err.print("\ndiffernt Modification glycosidic flag");return equiv;}
				if(bk1ed.getModification().isAglycone() != bk2ed.getModification().isAglycone()){System.err.print("\ndiffernt Modification glycosidic flag");return equiv;}
				System.err.print("|isRing: "+bk1ed.getModification().isRing()+" "+bk2ed.getModification().isRing());
//				if((bk1ed.getModification().isRing() == true && bk2ed.getModification().isRing() == false) || (bk1ed.getModification().isRing() == false && bk2ed.getModification().isRing() == true)) {System.err.print("\ndiffernt Modification glycosidic flag");return equiv;}
				if(bk1ed.getModification().isRing() != bk2ed.getModification().isRing()){System.err.print("\ndiffernt Modification glycosidic flag");return equiv;}
				System.err.print("|canOmitMAP: "+bk1ed.getModification().canOmitMAP()+" "+bk2ed.getModification().canOmitMAP());
//				if((bk1ed.getModification().canOmitMAP() == true && bk2ed.getModification().canOmitMAP() == false) || (bk1ed.getModification().canOmitMAP() == false && bk2ed.getModification().canOmitMAP() == true)) {System.err.print("\ndiffernt Modification glycosidic flag");return equiv;}
				if(bk1ed.getModification().canOmitMAP() != bk2ed.getModification().canOmitMAP()){System.err.print("\ndiffernt Modification glycosidic flag");return equiv;}
				System.err.print("|hasBackboneCarbonOrder: "+bk1ed.getModification().hasBackboneCarbonOrder()+" "+bk2ed.getModification().hasBackboneCarbonOrder());
				if(bk1ed.getModification().hasBackboneCarbonOrder() != bk2ed.getModification().hasBackboneCarbonOrder()){System.err.print("\ndiffernt Modification glycosidic flag");return equiv;}
// compare Linkage with Modification and ModificationAlternative of glycosidicLinkages

//compare Linkage
				int sizeOfBackboneEdgeLinkage1=bk1ed.getLinkages().size();
				int sizeOfBackboneEdgeLinkage2=bk2ed.getLinkages().size();
				System.err.print(" linkage: "+sizeOfBackboneEdgeLinkage1+" "+sizeOfBackboneEdgeLinkage2+"|");
				if(sizeOfBackboneEdgeLinkage1!=sizeOfBackboneEdgeLinkage2) {System.err.print("different size of LinkagePosition");return equiv;}
				for(int k=0;k<sizeOfBackboneEdgeLinkage1;k++) {
					LinkagePosition bk1edlk=bk1ed.getLinkages().get(k);
					LinkagePosition bk2edlk=bk2ed.getLinkages().get(k);
					System.err.print("\nLinkage: "+bk1edlk.getBackbonePosition()+" "+bk2edlk.getBackbonePosition()+"|"+bk1edlk.getModificationPosition()+" "+bk2edlk.getModificationPosition()+"|");
					System.err.print(bk1edlk.getDirection().getName()+" "+bk2edlk.getDirection().getName()+"|");
					if(bk1edlk.getBackbonePosition() != bk2edlk.getBackbonePosition()) {System.err.print("\ndifferent Backbone Position");return equiv;}
					if(bk1edlk.getModificationPosition() != bk2edlk.getModificationPosition()) {System.err.print("\ndifferent modificationPosition");return equiv;}
					if(bk1edlk.getDirection().getName() != bk2edlk.getDirection().getName()) {System.err.print("\ndiffernt direction");return equiv;}
					System.err.print(" upper: "+bk1edlk.getProbabilityUpper()+" "+bk2edlk.getProbabilityUpper());
					System.err.print(" lower: "+bk1edlk.getProbabilityLower()+" "+bk2edlk.getProbabilityLower());
					if(Math.abs(bk1edlk.getProbabilityUpper()-bk2edlk.getProbabilityUpper())>0.000001) {System.err.print("\ndifferent Probability Upper");return equiv;}
					if(Math.abs(bk1edlk.getProbabilityLower()-bk2edlk.getProbabilityLower())>0.000001) {System.err.print("\ndifferent Probability Lower");return equiv;}
					}
				}
			System.err.print("\n");
			}
		System.err.print("glycosidicModifications: \n");
		int sizeOfMod1=m1.size();
		int sizeOfMod2=m2.size();
		System.err.print("size of glycosidicModification: "+sizeOfMod1+" "+sizeOfMod2);
		if(sizeOfMod1!=sizeOfMod2) {System.err.print("different size of GlycosidicModification"); return equiv;}
		for(int i=0;i<sizeOfMod1;i++) {
			Modification m1mod=m1.get(i);
			Modification m2mod=m2.get(i);
			System.err.print("\nglycosidicModification|"+m1mod.getMAPCode()+"|"+m2mod.getMAPCode());
			if(m1mod.getMAPCode().compareTo(m2mod.getMAPCode())!=0) {System.err.print("differnet MAPCode in glycosidicModifications"); return equiv;}
			System.err.print("isAglycone: "+m1mod.isAglycone()+" "+m2mod.isAglycone()+"|");
			if(m1mod.isAglycone()!=m2mod.isAglycone()) {System.err.print("different Aglycone flag in glycosidicModifications");return equiv;}
			System.err.print("isGlycosidic: "+m1mod.isGlycosidic()+" "+m2mod.isGlycosidic()+"|");
			if(m1mod.isGlycosidic()!=m2mod.isGlycosidic()) {System.err.print("different Glycosidic flag in glycosidicModifications");return equiv;}
			System.err.print("isRing: "+m1mod.isRing()+" "+m2mod.isRing()+"|");
			if(m1mod.isRing()!=m2mod.isRing()) {System.err.print("different Ring flag in glycosidicModifications");return equiv;}
			int sizeOfEdge1=m1mod.getEdges().size();
			int sizeOfEdge2=m2mod.getEdges().size();
			if(sizeOfEdge1!=sizeOfEdge2) {System.err.print("different size of Edge in glycosidicModification");return equiv;}
			System.err.print("\n");
			for(int j=0;j<sizeOfEdge1;j++) {
				WURCSEdge m1moded = m1mod.getEdges().get(j);
				WURCSEdge m2moded = m2mod.getEdges().get(j);
				System.err.print("Edges: reverse "+m1moded.isReverse()+" "+m2moded.isReverse()+" anomeric "+m1moded.isAnomeric()+" "+m2moded.isAnomeric());
				if(m1moded.isReverse()!=m2moded.isReverse()) {System.err.print("different reverse flag in glycosidicMopdification");return equiv;}
				if(m1moded.isAnomeric()!=m2moded.isAnomeric()) {System.err.print("different anomeric flag in glycosidicMopdification");return equiv;}
				int sizeOfLink1=m1moded.getLinkages().size();
				int sizeOfLink2=m2moded.getLinkages().size();
				System.err.print(" linkage size: "+sizeOfLink1+" "+sizeOfLink2);
				if(sizeOfLink1!=sizeOfLink2) {System.err.print("differnt size of LinkagePosition in glycosidicModification");return equiv;}
				for(int k=0;k<sizeOfLink1;k++) {
					LinkagePosition m1modedlk=m1moded.getLinkages().get(k);
					LinkagePosition m2modedlk=m2moded.getLinkages().get(k);
					System.err.print(" Linkage "+m1modedlk.getBackbonePosition()+" "+m2modedlk.getBackbonePosition()+"|");
					System.err.print(m1modedlk.getModificationPosition()+" "+m2modedlk.getModificationPosition()+"|");
					if(m1modedlk.getBackbonePosition()!=m2modedlk.getBackbonePosition()){System.err.print("differnet Backbone Position");return equiv;}
					if(m1modedlk.getModificationPosition()!=m2modedlk.getModificationPosition()){System.err.print("differnet Backbone Position");return equiv;}
					}
				}
			}
		System.err.print("glycosidicModificationsAlternative: \n");
		int sizeOfModalt1=ma1.size();
		int sizeOfModalt2=ma2.size();
		System.err.print("size of glycosidicModificationAlternative: "+sizeOfModalt1+" "+sizeOfModalt2);
		if(sizeOfModalt1!=sizeOfModalt2) {System.err.print("different size of GlycosidicModificationAlternative"); return equiv;}
		for(int i=0;i<sizeOfModalt1;i++) {
			Modification m1modalt=ma1.get(i);
			Modification m2modalt=ma2.get(i);
			System.err.print("\nglycosidicModification|"+m1modalt.getMAPCode()+"|"+m2modalt.getMAPCode());
			if(m1modalt.getMAPCode().compareTo(m2modalt.getMAPCode())!=0) {System.err.print("differnet MAPCode in glycosidicModifications"); return equiv;}
			System.err.print("isAglycone: "+m1modalt.isAglycone()+" "+m2modalt.isAglycone()+"|");
			if(m1modalt.isAglycone()!=m2modalt.isAglycone()) {System.err.print("different Aglycone flag in glycosidicModifications");return equiv;}
			System.err.print("isGlycosidic: "+m1modalt.isGlycosidic()+" "+m2modalt.isGlycosidic()+"|");
			if(m1modalt.isGlycosidic()!=m2modalt.isGlycosidic()) {System.err.print("different Glycosidic flag in glycosidicModifications");return equiv;}
			System.err.print("isRing: "+m1modalt.isRing()+" "+m2modalt.isRing()+"|");
			if(m1modalt.isRing()!=m2modalt.isRing()) {System.err.print("different Ring flag in glycosidicModifications");return equiv;}
			int sizeOfEdge1=m1modalt.getEdges().size();
			int sizeOfEdge2=m2modalt.getEdges().size();
			if(sizeOfEdge1!=sizeOfEdge2) {System.err.print("different size of Edge in glycosidicModification");return equiv;}
			System.err.print("\n");
			for(int j=0;j<sizeOfEdge1;j++) {
				WURCSEdge m1modalted = m1modalt.getEdges().get(j);
				WURCSEdge m2modalted = m2modalt.getEdges().get(j);
				System.err.print("Edges: reverse "+m1modalted.isReverse()+" "+m2modalted.isReverse()+" anomeric "+m1modalted.isAnomeric()+" "+m2modalted.isAnomeric());
				if(m1modalted.isReverse()!=m2modalted.isReverse()) {System.err.print("different reverse flag in glycosidicMopdification");return equiv;}
				if(m1modalted.isAnomeric()!=m2modalted.isAnomeric()) {System.err.print("different anomeric flag in glycosidicMopdification");return equiv;}
				int sizeOfLinkalt1=m1modalted.getLinkages().size();
				int sizeOfLinkalt2=m2modalted.getLinkages().size();
				System.err.print(" linkage size: "+sizeOfLinkalt1+" "+sizeOfLinkalt2);
				if(sizeOfLinkalt1!=sizeOfLinkalt2) {System.err.print("differnt size of LinkagePosition in glycosidicModification");return equiv;}
				for(int k=0;k<sizeOfLinkalt1;k++) {
					LinkagePosition m1modaltedlk=m1modalted.getLinkages().get(k);
					LinkagePosition m2modaltedlk=m2modalted.getLinkages().get(k);
					System.err.print(" Linkage "+m1modaltedlk.getBackbonePosition()+" "+m2modaltedlk.getBackbonePosition()+"|");
					System.err.print(m1modaltedlk.getModificationPosition()+" "+m2modaltedlk.getModificationPosition()+"|");
					if(m1modaltedlk.getBackbonePosition()!=m2modaltedlk.getBackbonePosition()){System.err.print("differnet Backbone Position");return equiv;}
					if(m1modaltedlk.getModificationPosition()!=m2modaltedlk.getModificationPosition()){System.err.print("differnet Backbone Position");return equiv;}
					}
				}
			}
		equiv=true;
		return equiv;
		}
	}
