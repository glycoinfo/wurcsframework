package org.glycoinfo.WURCSFramework.exec;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.WURCSValidator;

public class WURCSValidatorTest {
	public static void main(String[] args) {
		LinkedList<String> t_aWURCSList = new LinkedList<String>();
		t_aWURCSList.add("WURCS=2.0/1,1,0/[u2122h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[u2g22h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[u2L22h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[u2122h_1-5]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[u2122h-1x]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[u2122h-1x_1-5]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[hU2122h-1x_2-6]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[hU2122h_2-6]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a622h-1b_1-5]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a622h-1b_1-5_2*CO]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[o2122h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[o2122h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[oddddh]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[o2<x>22h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[o2<xx>22h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2122h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2122h_1-5]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2122h-1a_1-5]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2122h-?a_1-5]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[h2122h-1a_1-5]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[h2122h_1-5]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[h2122h-1a]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[h2122h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2122h-1a_1-5]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2122h-1g_1-5]/1/");
		t_aWURCSList.add("WURCS=2.0/1,3,4/[a2122h-1a_1-5]/1-1-1/a4-b1_b4-c1_c3-b4_b4-a3");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2OO2h-1a_2-5]/1/");
		t_aWURCSList.add("WURCS=2.0/1,2,1/[a2122h-1a_1-5]/1-1/a1-a2-b3");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[u2UO2h-1a_2-5]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2122a-1a]/1/");
		t_aWURCSList.add("WURCS=2.0/3,31,30/[a2122h-1x_1-5_2*NCC/3=O][a1122h-1x_1-5][a1122h-1x_1-5_?*OPO/3O/3=O]/1-1-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-3-2-2-3-2-2/a?-b1_b?-c1_c?-d1_c?-B1_d?-e1_d?-z1_e?-f1_e?-w1_f?-g1_f?-r1_g?-h1_g?-o1_h?-i1_h?-l1_i?-j1_j?-k1_l?-m1_l?-n1_o?-p1_p?-q1_r?-s1*OPO*/3O/3=O_r?-u1_s?-t1_u?-v1_w?-x1_x?-y1_z?-A1_B?-C1_B?-E1_C?-D1\n");
		t_aWURCSList.add("WURCS=2.0/3,3,2/[a2122A-1x_1-5][a211h-1x_1-4][a211h-1x_1-?]/1-2-3/a?-b1_a?-c1");
		t_aWURCSList.add("WURCS=2.0/4,4,4/[<Q>-?a_7-9*OC^XO*/3CO/6=O/3C_5*OCC/3=O][a2122h-1a_1-5][a2122h-1b_1-5][a1122h-1b_1-5]/1-2-3-4/a4-b1_b4-c1_c4-d1_a2-d3~n");
		t_aWURCSList.add("WURCS=2.0/2,4,3/[a122h-1x_1-4][<Q>]/1-1-2-2/a5-b1_b3-c1_b5-d1");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[h2122222222222222h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2122ah-1a_1-5]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[hU122h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[hO122h_2*=N]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[ha122h-2x_2-6]/1/");
		t_aWURCSList.add("WURCS=2.0/1,2,0/[ha122h-2x_2-6]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[ha122h-1x_2-6]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[ha522h-2x_2-6]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[A1EE1h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[A1EE1h-1x]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[A1EE1h_2-4]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[A1EZ1h]/1/");
		t_aWURCSList.add("WURCS=2.0/1,2,1/[a2122h-1b_1-5]/1-1/a?-b3*OSO*/3=O/3=O");
		t_aWURCSList.add("WURCS=2.0/3,4,4/[hxh][a2122h-1x_1-5][a2122h-1a_1-5]/1-2-3-3/a3n2-b1n1*1NCCOP^XO*2/6O6=O_b4-c1_c4-d1_c1-c4~n");
		t_aWURCSList.add("WURCS=2.0/3,4,4/[hxh][a2122h-1x_1-5][a2122h-1a_1-5]/1-2-3-3/a3n2-b1n1*1NCCOP^XO*3/6O6=O_b4-c1_c4-d1_c1-c4~n");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[Ad2d112h_3-7_5n2-6n1*1O(CCC^ECC^ZC$3)/6NC(CC^ZCC^ZNC$11)/4*2/10=O_1*NC(C^ZCC^ZCC^EC$4)/8F/5F]/1/");
		t_aWURCSList.add("WURCS=2.0/4,12,10+/[a2122h-1x_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5]/1-2-3-4-4-4-4-4-4-4-4-4/a4-b1_b4-c1_c3-d1_c6-h1_d2-e1_d6-g1_e2-f1_h3-i1_h6-j1_j2-k1");
		t_aWURCSList.add("WURCS=2.0/4,12,10/[a2122h-1x_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5]/1-2-3-4-4-4-4-4-4-4-4-4/a4-b1_b4-c1_c3-d1_c6-h1_d2-e1_d6-g1_e2-f1_h3-i1_h6-j1_j2-k1");
		t_aWURCSList.add("WURCS=2.0/4,12,11/[a2122h-1x_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5]/1-2-3-4-4-4-4-4-4-4-4-4/a4-b1_b4-c1_c3-d1_c6-h1_d2-e1_d6-g1_e2-f1_h3-i1_h6-j1_j2-k1");
		t_aWURCSList.add("WURCS=2.0/5,15,8/[a2122h-1x_1-5_2*NCC/3=O][a1122h-1x_1-5][a2112h-1x_1-5][Aad21122h-2x_2-6_5*NCC/3=O][Aad21122h-2x_2-6_5*NCCO/3=O]/1-1-2-2-1-3-2-1-3-1-3-4-4-4-5/a?-b1_b?-c1_c?-d1_c?-g1_d?-e1_e?-f1_g?-h1_h?-i1");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2nh-1a_1-4_2*CO]/1/");
		t_aWURCSList.add("WURCS=2.0/2,13,12/[a2122h-1x_1-5_2*NCC/3=O][a1122h-1x_1-5]/1-2-2-2-2-2-2-2-2-2-2-2-2/a?-b1_b?-c1_b?-i1_c?-d1_c?-g1_d?-e1_d?-f1*OPO*/3O/3=O_g?-h1_i?-j1_i?-l1_j?-k1_l?-m1");
		t_aWURCSList.add("WURCS=2.0/2,13,12/[a2122h-1x_1-5_2*NCC/3=O][a1122h-1x_1-5]/1-2-2-2-2-2-2-2-2-2-2-2-2/a?-b1_b?-c1_b?-i1_c?-d1_c?-g1_d?-e1_d?-f1*OPO*/3O*/3=O_g?-h1_i?-j1_i?-l1_j?-k1_l?-m1");
		t_aWURCSList.add("WURCS=2.0/2,13,12/[a2122h-1x_1-5_2*NCC/3=O][a1122h-1x_1-5]/1-2-2-2-2-2-2-2-2-2-2-2-2/a?-b4_b4-c1_b4-i1_c?-d1_c?-g1_d?-e1_d?-f1*OPO*/3O/3=O_g?-h1_i?-j1_i?-l1_j?-k1_l?-m1");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2122h-1x_1-5_2*NCC/3=O]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[a2122h-1x_1-5_2*]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[axxxxh-1x_1n2-2n1*1OC(CCCC^ZCC$4)/8O/7OC/6O/5*2/3=O_1-5]/1/");
		t_aWURCSList.add("WURCS=2.0/1,1,0/[axxxxh-1x_1n2-2n1*1OC(CCCC^ZCC$4)/8O/7OC/6O/5*/3=O_1-5]/1/");
		t_aWURCSList.add("WURCS=2.0/2,6,5/[o222h][a222h-1x_1-4]/1-2-2-2-2-2/c1-d1_e1-f1_a?-b1_b?-f?*OPO*/3O/3=O_c?-e?*OPO*/3O/3=O");
		t_aWURCSList.add("WURCS=2.0/2,6,5/[o222h][a222h-1x_1-4]/1-2-2-2-2-2/c1-d1_e1-f1_a?-b1_b?-e?*OPO*/3O/3=O_c?-f?*OPO*/3O/3=O");
		t_aWURCSList.add("WURCS=2.0/1,2,1/[11m_1*OCC/3=O]/1-1/a1n3-a2n1-b1n4-b2n2*1OC(CCCC^ZCCCC^ZCCC^ECC^ECC$9/18$13/8C^ECC^ZCC$4/22$7)/23O/21OC/19OC/16OC/14OC/12O/11CO*2/10*4/5*3");
		t_aWURCSList.add("WURCS=2.0/3,3,1/[A11dCd12dd1d1d1dA][a1122m-1b_1-5_3*N][ad111m-1x_1-5]/1-2-3/a2n4-a5d1-a17n3-b1n2-c1n5*1OC^R*4/3CC^RO*2/6C=^ECC=^ECC=^ECC=^ECCCC=^ECC=^ECC^SC^RO*5/24C^SC^SO*3/28C/27C/23C");
		t_aWURCSList.add("WURCS=2.0/3,3,1/[A11dCd12dd1d1d1dA][a1122m-1b_1-5_3*N][ad111m-1x_1-5]/1-2-3/a2n4-a5d1-a17n3-b1n2-c1n5*1OC^R*4/3CC^RO*2/6C=^ECC=^ECC=^ECC=^ECCCC=^ECC=^ECC^SC^RO*6/24C^SC^SO*3/28C/27C/23C");
//
		for ( String t_strWURCS : t_aWURCSList ) {
			System.out.println(t_strWURCS+": ");
			WURCSValidator validator=new WURCSValidator();
			validator.start(t_strWURCS);
			if(validator.getTheNumberOfErrors()==0) System.out.println(validator.getStandardWURCS());
			System.out.println("the number of errors: "+validator.getTheNumberOfErrors());
			for(String er: validator.getErrors()) System.out.println("	Error:   "+er);
			for(String wa: validator.getWarnings()) System.out.println("	Warning: "+wa);
			System.out.println();
			}
		}
	}
