
def also_bought(fn):
    """
    @param fn csv file name
    @return dictionary[sku] = dict[sku_also_bought:times_also_bought]
    """
    users = dict()
    skus = set()
    
    with open(fn,"r") as fin:
        for i,  line in enumerate(fin):
            
            if not i: continue

            row = line.strip('\n').split(',')
            email = str(row[1])
            sku = str(row[3])
            skus.add(sku)

            if email not in users: 
                users[email] = set()
            users[email].add(sku)

    sku_count = dict()

    for sku in skus: 
        sku_count[sku] = dict()
        for email, skus_bought in users.items(): 

            if sku not in skus_bought: continue

            for also_bought in skus_bought:
                if also_bought == sku: continue
                if also_bought not in sku_count[sku]:
                    sku_count[sku][also_bought] = 1
                else:
                    sku_count[sku][also_bought] += 1

    return sku_count
