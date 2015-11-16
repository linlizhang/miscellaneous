selected_words = ['awesome', 'great', 'fantastic', 'amazing', 'love', 'horrible', 'bad', 'terrible', 'awful', 'wow', 'hate']
def awesome_count(item):
    if 'awesome' in item:
        return item['awesome']
    else:
        return 0
#Generate 'awesome' column
products['awesome'] = products['count_words'].apply(awesome_count)

def word_count(item, word):
    if word in item:
        return item[word]
    else:
        return 0
#Generate 'awesome' column
products[word] = products['count_words'].apply(word_count)


selected_words_1 = ['great', 'fantastic', 'amazing', 'love', 'horrible', 'bad', 'terrible', 'awful', 'wow', 'hate']
for word in selected_words_1:
    backup[word] = backup['count_words'].apply(lambda x: word_count(x, word))

# sum of the sentiment word count 
sumlist = []   
for word in selected_words:
    sumlist.append(products[word].sum())  
