awk '{
    for(i=1;i<=NF;i++){
        a[NR,i] = $i
    }
    if (NF > p) {
        p=NF
    }
}

END {
    for(row=1;row<=p;row++){
        str=a[1,row]
        for(col=2;col<=NR;col++){
            str=str" "a[col,row]
        }
        print str
    }
}' /home/cloudera/file.txt
