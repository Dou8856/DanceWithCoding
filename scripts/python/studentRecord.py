class Student:
    def __init__(self, sid = None, classStanding = None, firstName = None, lastName = None, gpa = None):
        self.sid = sid
        self.classStanding = classStanding
        self.firstName = firstName
        self.lastName = lastName
        self.gpa = gpa
    def __str__(self):
        return """
        Student ID: %s
        ClassStanding: %s
        FirstName: %s
        LastName: %s
        gpa: %s
        """%(self.sid, self.classStanding, self.firstName, self.lastName, self.gpa)

    def getClass(self):
        classList = ["Freshman", "Sophomore", "Junior", "Senior"]
        return classList[self.classStanding]
    def getName(self, reversed):
        if reversed:
            return self.lastName + " " + self.firstName
        else:
            return self.firstName + " " + self.lastName
class Class:
    def __init__(self):
        self.studentList = []
    def addStudent(self, student):
        self.studentList.append(student)
    def findStudent(self, name = None, sid = None):
        if name is not None:
            lookupList = []
            for student in self.studentList:
                if name.upper() in student.getName(False).upper():
                    lookupList.append(student)
            return lookupList
        else:
            for student in studentList:
                if student.sid == sid:
                    return student
    def __str__(self):
        discript = "Students in this class are:\n"
        for student in self.studentList:
            discript = discript + student.getName(False) + "\n"
        return discript


