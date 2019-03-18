BEGIN{FS=":"}
/^G.......:/{id=$1;
	getline;
	before=$1;
	getline;
	after=$1;
	printf"%s%s%s\n",id,before,after;
	}
