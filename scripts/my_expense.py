fname = "2015.txt"
def print_list(expense_list):
    total = 0
    for key in expense_list.keys():
        total = total + expense_list[key]
        print "{0:10} {1:2}".format(key, expense_list[key])
    border="-"*20
    print "{0:10}\n{1:10} {2:2}".format(border, "Total", total)
    return total

def montly_expense(month):
    fhandle = open(fname)
    #Find the month and calculate weekly expense and do summary
    print "Has not been implemented yet!"


def weekly_expense(week=None):
    if week is None:
        week = raw_input("Enter the week number ")

    week = week.lower()
    fhandle = open(fname)
    # initialize a dictionary for expense
    expense = {}

    # Read each line in file and skip the head line
    #next(fhandle) #skip the first line
    #find the week number we are looking for
    for line in fhandle:
        #find the week and read each line
        line=line.strip()
        if line.lower().startswith(week):
            for line in fhandle:
                if not line.startswith("----"):
                    l = line.split()
                    category = l[2].lower()
                    cost = float(l[1])
                    if category not in expense.keys():
                        expense[category] = cost
                    else:
                        expense[category] = expense[category] + cost
                else:
                    print_list(expense)
                    break
    return expense

option = raw_input("Weekly summary(1) or Monthly summary(2): ")
try:
    opt = int(option)
except:
    print "Please enter valid option!"
if opt == 1:
    weekly_expense()
elif opt == 2:
    monthly_expense()
