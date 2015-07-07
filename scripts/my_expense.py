def print_list(expense_list):
    total = 0
    for key in expense_list.keys():
        total = total + expense_list[key]
        print "{0:10} {1:2}".format(key, expense_list[key])
    return total


fname = raw_input("Enter file name: ")
fhandle = open(fname)
# initialize a dictionary for expense
expense = {}

next(fhandle) #skip the first line
# Read each line in file and skip the the head line
for line in fhandle:
    #get elements in each line as a list
    l = line.split()
    category = l[2].lower()
    cost = float(l[1])
    if category not in expense.keys():
        expense[category] = cost
    else:
        expense[category] = expense[category] + cost
print print_list(expense)
