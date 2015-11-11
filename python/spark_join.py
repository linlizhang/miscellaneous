def split_fileA(line):
    line = line.strip()
    word, count = line.split(",")
    count = int(count)
 
    return (word, count)
    
#Mar-03 about,8    
import sys    
def split_fileB(line):
    line = line.strip()
    date, subStr = line.split(" ")
    word, count_string = subStr.split(",")
    return (word, date + " " + count_string)
    
    
#PostModern_Show,38
show_files=sc.textFile("input_5_2/join2_gennum?.txt")

def split_show_views(line):
    line = line.strip()
    show, views = line.split(",")
    return (show, views)
    
show_view_data = show_files.map(split_show_views)
#Baked_News,BAT
channel_file=sc.textFile("input_5_2/join2_genchan?.txt")
def split_show_channel(line):
    line = line.strip()
    show, channel = line.split(",")
    return (show, channel)
    
show_channel_data = channel_file.map(split_show_channel)
    

#(u'PostModern_Cooking', (u'DEF', u'1038'))
joined_dataset = show_channel_data.join(show_view_data)


def extract_channel_views(show_views_channel):
    channel_view_tuple = show_views_channel[1]
    channel = channel_view_tuple[0]
    views = channel_view_tuple[1]
    views = int(views)
    return (channel, views)
    
channel_views = joined_dataset.map(extract_channel_views)  

 
def some_function(a, b):
    return a + b 
    
r=channel_views.reduceByKey(some_function)

invalid = sc.accumulator(0)
def count_invalid(element):
    try:
        int(element)
    except:
        invalid.add(1)
        
        
d = sc.parallelize(["3", "23", "s"]).foreach(count_invalid)