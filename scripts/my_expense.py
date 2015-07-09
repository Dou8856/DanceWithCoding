fname = "2015.txt"
#TODO: Optimization
def weekly_expense(week=None):
    if week is None:
        week = raw_input("Enter the week number ")

    fhandle = open(fname)
    # initialize a dictionary for expense
    expense = {}

    # Read each line in file and skip the head line
    #next(fhandle) #skip the first line
    #find the week number we are looking for
    for line in fhandle:
        #find the week and read each line
        line=line.strip()

#Some logic error: when should we statr to calculate and stop calculating
        if line.lower().startswith("week") and line.split()[len(line.split()) - 1] == week:
            for line in fhandle:
                if not line.startswith("----"):
                    expense[line.split()[2].lower()] = expense.get(line.split()[2].lower(), 0) + float(line.split()[1])
                else:
                    print "Week%s"%week
                    print_list(expense)
                    break
    return expense
#-------------------EndOfFunction----------------------------------

def print_list(expense_list):
    total = 0
    for key in expense_list:
        total = total + expense_list[key]
        print "{0:10} {1:2}".format(key, expense_list[key])
    border="-"*20
    print "{0:10}\n{1:10} {2:2}\n".format(border, "Total", total)
    return total
#-------------------EndOfFunction----------------------------------

#TODO: Add the cost which is not included in weekly expenses
def monthly_expense(month=None):
    if month is None:
        month = raw_input("Enter the month: ")
        if "-" in month:
            month = month.split("-")[len(month.split("-") - 1)]
    list_of_weeks = list()
    fhandle = open(fname)
    expense = {}
    #Find the month and calculate weekly expense and do summary
    #Find the month which is being summarized
    for line in fhandle:
        if line.lower().startswith("month"):
            m = line.split("-")[len(line.split()) - 1]#get the last digit which represents month
            if int(m) == int(month):
                for line in fhandle:
                    if "endofweek" in line.lower():
                        for line in fhandle:
                            #After end of week, we check the start condition and end condition
                            if "week" not in line.lower() and not line.startswith("==="):
                                #split_list = line.split()
                                expense[line.split()[2]] = expense.get(line.split()[2], 0) + float(line.split()[1])
                            else:
                                break
                    if line.lower().startswith("week"):
                        #put the week number in the week list
                        list_of_weeks .append(line.split()[1])
                        print line
                    elif line.startswith("==="):
                        break
    for w in list_of_weeks:
        week_expense = weekly_expense(w)
        for key in week_expense:
            expense[key] =  expense.get(key, 0) + week_expense[key]
    print """
Monthly Summary
-----------------"""
    return expense
#-------------------EndOfFunction----------------------------------



option = raw_input("Weekly summary(1) or Monthly summary(2): ")
try:
    opt = int(option)
except:
    print "Please enter valid option!"
if opt == 1:
     weekly_expense()
elif opt == 2:
    print_list( monthly_expense())
else:
    print "Not Valid Option!"

