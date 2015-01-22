package org.glycoinfo.WURCSFramework.wurcsRDF;

public class Predicate {

	// # Main Glycan structure Triplet
	// glycan has_components ms1, ms2, ms3, ms4, ...
	// <http://rdf.glycoinfo.org/glycan/G97690MS> has_components <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> .
	String has_components;
	
	// # Main component Triplet  
	// ms1 is_glycosidicLinkage ms2, ms3, ...
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> is_glycosidicLinkage <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> 
	// ms1 is_connected ms2, ms3, ...
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> is_connected <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> 
	String is_glycosidicLinkage;
	String is_connected;
	
	// monosaccharide
	// ms1 is_monosaccharide ms1
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO>
	// 12122h-1b_1-5_2*NCC%2F3%3DO
	String is_monosaccharide;
	// 12122h
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> has_skeletonCode "12122h"^^xsd:string .
	String has_skeletonCode;
	String has_carbonDescriptor;
	
	// skeletonCode 12122h	=>	2122
	// carbon descriptor: a-zA-Z[^hdm?] => "", 3,5 => 1, 4,6 =>. 2, d => d, m => m, h => h,  ? => ?
	// RES of GlycoCT
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> has_sugar_code "2122h"^^xsd:string .
	String has_monosaccharide_code;
	// 12122h-1b_1-5_2*NCC%2F3%3DO => // 12122h-1b
	String has_base_type;
	
	// if Hexose => 6
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> carbon_number_of_monosaccharide	"6"^^xsd:integer ;
	int carbon_number_of_monosaccharide;
	// type of monosaccharide
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> is_hexose <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO>
	String is_triose;
	String is_tetraose;
	String is_pentose;
	String is_hexose;
	String is_heptaose;
	String is_octaose;
	String is_nonanose;
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> is_aldose <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO>
	String is_aldose;
	String is_ketose;
	String has_ald_or_keto;
	// anomer
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> has_anomeric_beta <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO>
	String has_anomeric_beta;
	String has_anomeric_alpha;
	String has_anomeric_unknown;
	// anomer position
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> anomeric_position_1 <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO>
	String anomeric_position_1;
	String anomeric_position_2;
	String anomeric_position_3;
	String anomeric_position_4;
	String anomeric_position_5;
	String anomeric_position_6;
	String anomeric_position_7;
	String anomeric_position_8;
	String anomeric_position_9;
	String anomeric_position_others;
	//
	// modification
	// ring
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> is_pyranose <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO>
	String is_pyranose;
	String is_furanose;
	// 1-5, 2*NCC%2F3%3DO
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> has_MOD "2*NCC%2F3%3DO"^^xsd:string .
	String has_MOD;
	// 2, ?, ...
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> has_MOD_position "2"^^xsd:string .
	String has_MOD_position;
	// *O*, *NCC%2F3%3DO
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> has_MAP "*NCC%2F3%3DO"^^xsd:string .
	String has_MAP;
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> is_amino_sugar <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO>	
	String is_amino_sugar;
	String is_uronic_acid;
	// <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO> has_NAc <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12122h-1b_1-5_2*NCC%2F3%3DO>
	String has_NAc;
	String has_sulfate;
	String has_phosphate;
	String has_OAc;
	// 1
	String has_MAP_position;
	
	// LIN
	String linkedCarbon_fuzzy;
	String linkedCarbon_unknown;
	String linkedCarbon_1;
	String linkedCarbon_2;
	String linkedCarbon_3;
	String linkedCarbon_4;
	String linkedCarbon_5;
	String linkedCarbon_6;
	String linkedCarbon_7;
	String linkedCarbon_8;
	String linkedCarbon_9;
	String linkedCarbon_others;

}
