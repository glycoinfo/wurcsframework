package org.glycoinfo.WURCSFramework.wurcsRDF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.rdf.model.RDFReaderF;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class WURCSRDFModel {
	
	private Model t_oModel = null;

	public WURCSRDFModel(){
		this.t_oModel = createDefaultModel(true);
	}
	
	public WURCSRDFModel(boolean set_default){
		this.t_oModel = createDefaultModel(set_default);
	}
	
	public void addTriple(WURCSTriple triple){
		Resource subject = this.t_oModel.createResource(triple.getSubject());
		Property predicate = this.t_oModel.createProperty(triple.getPredicate());
		RDFNode object = this.createRDFNode(triple.getObject());
		if( subject != null && predicate != null && object != null ){
			this.t_oModel.add(subject, predicate, object);
		}
	}
	
	public void removeTriple(WURCSTriple triple){
		Resource subject = this.t_oModel.createResource(triple.getSubject());
		Property predicate = this.t_oModel.createProperty(triple.getPredicate());
		RDFNode object = this.createRDFNode(triple.getObject());
		if( subject != null && predicate != null && object != null ){
			this.t_oModel.remove(subject, predicate, object);
		}
	}
	
	public void addPrefix(String prefix, String prefix_url){
		this.t_oModel.setNsPrefix(prefix, prefix_url);
	}
	
	public HashMap<String,String> getPrefix(){
		Map<String,String> map = this.t_oModel.getNsPrefixMap();
		HashMap<String,String> result_map = null;
		if( map != null ){
			result_map = new HashMap<String,String>();
			Iterator<String> it = map.keySet().iterator();
			while(it.hasNext()){
				String prefix = it.next();
				String url = map.get(prefix);
				if( url != null ){
					result_map.put(prefix, url);
				}
			}
		}
		return result_map;
	}

	public String get_RDF(String format){
		String result_str = null;
		try{
			StringWriter sw = new StringWriter();
			this.t_oModel.write(sw, format);
			result_str = sw.toString();
			sw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result_str;
	}

	public Model get_Model(){
		return this.t_oModel;
	}

	/**
	public static String getTripleRDF(WURCSTriple[] triples, String format){
		Model model = getTripleModel(triples);
		String result_str = null;
		if( model != null ){
			try{
				StringWriter sw = new StringWriter();
				model.write(sw, format);
				result_str = sw.toString();
				sw.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result_str;
	}

	public static Model getTripleModel(WURCSTriple[] triples){
		Model model = (new RDFModel()).createTripleModel(triples);
		return model;
	}

	private Model createTripleModel(WURCSTriple[] triples){
		// TODO
		return this.model;
	}
	**/
	
	private RDFNode createRDFNode(Object obj){
		if(obj instanceof  String){
			return this.t_oModel.createTypedLiteral((String)obj, XSDDatatype.XSDstring);
		}else if( obj instanceof Integer){
			return this.t_oModel.createTypedLiteral((Integer)obj, XSDDatatype.XSDinteger);
		}else if( obj instanceof Double){
			return this.t_oModel.createTypedLiteral((Double)obj, XSDDatatype.XSDdouble);
		}else if( obj instanceof Boolean){
			return this.t_oModel.createTypedLiteral((Boolean)obj, XSDDatatype.XSDboolean);
		}else if( obj instanceof Character){
			return this.t_oModel.createTypedLiteral((Character)obj, XSDDatatype.XSDstring);
		}else if( obj instanceof URL){
			return this.t_oModel.createResource(((URL)obj).toString());
		}
		return null;
	}

	public static Model createDefaultModel(boolean set_default){
		Model model = ModelFactory.createDefaultModel();
		if( set_default ){
			PrefixList[] prefix_list = PrefixList.values();
			for( int i=0; i<prefix_list.length; i++ ){
				model.setNsPrefix(prefix_list[i].getPrefix(), prefix_list[i].getPrefixURI());
			}
		}
		return model;
	}
	
	public static WURCSTriple createResourceTriple(String subject, String predicate, String url_str){
		try{
			WURCSTriple tpl = new WURCSTriple();
			tpl.setSubject(subject);
			tpl.setPredicate(predicate);
			URL url = new URL(url_str);
			tpl.setObject(url);
			return tpl;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static WURCSTriple createLiteralTriple(String subject, String predicate, Object object){
		try{
			WURCSTriple tpl = new WURCSTriple();
			tpl.setSubject(subject);
			tpl.setPredicate(predicate);
			tpl.setObject(object);
			return tpl;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args){
		org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray wa;
		String text = readFile("D:/sde/eclipse43ee/git/glytoucan-ws/src/work/java/org/glycoinfo/WURCSFramework/testresource/20150302result-GlyTouCan_GlycoCTmfWURCS.txt");
		String[] lines = text.split("\n");
		for( int  i=0; i<lines.length; i++ ){
			try {
				String[] values = lines[i].split("\t");
				if( values.length != 2 || !values[1].startsWith("WURCS=") )continue;
				System.out.println(values[0]+":"+i);
				wa = (new org.glycoinfo.WURCSFramework.util.array.WURCSImporter()).extractWURCSArray(values[1]);
//				WURCSRDFModelGlycan wcmodel1 = new WURCSRDFModelGlycan(values[0], wa);
				WURCSRDFModelMs wcmodel1 = new WURCSRDFModelMs(wa.getUniqueRESs());
				System.out.println(wcmodel1.get_RDF("TURTLE"));
				System.out.println("####################################################################");
				
				/**
				WURCSExporterRDF wcexp = new WURCSExporterRDF();
				wcexp.setWURCSrdfTriple(values[0], wa, false);
				String rdf_str1 = wcmodel1.get_RDF("TURTLE");
//				String rdf_str2 = wcexp.getWURCS_RDF();
				String rdf_str2 = wcexp.getWURCS_monosaccharide_RDF();
				Model model1 = getModelfromString(rdf_str1, false);
				Model model2 = getModelfromString(rdf_str2, true);
				long cnt1 = model1.size();
				long cnt2 = model2.size();
				if( model1.containsAll(model2) ){
					if( model2.containsAll(model1) ){
					}else{
						System.out.println("Err");
						Model dif_model = model2.difference(model1);
						long dif_cnt = dif_model.size();
						StmtIterator stit = dif_model.listStatements();
						while(stit.hasNext()){
							Statement stm = stit.next();
							System.out.println(stm.getSubject()+"\t"+stm.getPredicate()+"\t"+stm.getObject());
						}
						System.out.println("####################################################################");
						dif_model = model1.difference(model2);
						dif_cnt = dif_model.size();
						stit = dif_model.listStatements();
						while(stit.hasNext()){
							Statement stm = stit.next();
							System.out.println(stm.getSubject()+"\t"+stm.getPredicate()+"\t"+stm.getObject());
						}
					}
				}else{
					System.out.println("Err");
					Model dif_model = model2.difference(model1);
					long dif_cnt = dif_model.size();
					StmtIterator stit = dif_model.listStatements();
					while(stit.hasNext()){
						Statement stm = stit.next();
						System.out.println(stm.getSubject()+"\t"+stm.getPredicate()+"\t"+stm.getObject());
					}
					System.out.println("####################################################################");
					dif_model = model1.difference(model2);
					dif_cnt = dif_model.size();
					stit = dif_model.listStatements();
					while(stit.hasNext()){
						Statement stm = stit.next();
						System.out.println(stm.getSubject()+"\t"+stm.getPredicate()+"\t"+stm.getObject());
					}
				}
				 /**/
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static Model getModelfromString(String rdf_str, boolean use_ns){
		Model model = createDefaultModel(use_ns);
		Map<String,String> ns_map = model.getNsPrefixMap();
		java.util.Iterator<String> it = ns_map.keySet().iterator();
		while(it.hasNext()){
			String ns = it.next();
			String url = ns_map.get(ns);
			rdf_str = "@prefix "+ns+": <"+url+"> .\n" + rdf_str;
		}
		
		RDFReader rr = model.getReader("TURTLE");
		java.io.InputStream bais = new java.io.ByteArrayInputStream(rdf_str.getBytes());  
		rr.read(model,bais,"");
		return model;
	}
	
	protected static String readFile(String filename){
		StringBuffer sb = new StringBuffer();
		File ifile = new File(filename);
		if( !ifile.exists() ) return null;
		try {
			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader inFile = new InputStreamReader(fis,"UTF-8");
			BufferedReader inBuffer = new BufferedReader(inFile);

			String line;
			while ((line = inBuffer.readLine()) != null) {
				sb.append(line+System.getProperty("line.separator"));
			}

			inBuffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// UTF-8の時は先頭２バイトがBOM(Byte Order Mark)の場合がある
		if( sb.length()>0 ){
			int c0 = sb.codePointAt(0);
			if( c0 == 0xfeff || c0 == 0xfffe ){
				sb.replace(0, 1, "");
			}
		}
		return sb.toString();
	}
}
