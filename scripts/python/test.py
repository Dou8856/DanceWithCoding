from random import *
print random()
print randint(0, 100)
print uniform(1, 10)
#shuffle the list 
i = 0
l = []
while i < 10:
    l.append(randint(1,10))
    i=i+1
print l
l.sort()
print l
shuffle(l)
print l

print sample(l, 2)
print type(sample(l, 2))

#Class
class Student(object):
    #Private variables
    __name = ""
    __sid = ""
    class_name = ""

    def __init__(self, name, sid):
        self.changeName(name)
        self.setId(sid)
        self.class_name = "Student"
        print "Student",self.__name," profile is created!"

    def introduceSelf(self):
        print "Hello my name is",self.__name

    def printClassName(self):
        print "class_name =", self.class_name

#Encapsulation, python doesn't have protected method 
    #As Java, private method is only accessible within class__update() cannot be accessed from object
    def changeName(self, name):
       self.__name = name
    def getName(self):
        return self.__name
    def setId(self, sid):
        self.__sid = sid
    def getId(self):
        return self.sid
    #Method overloading, by setting default value of it
    def sayHello(self, name=None):
        if name is not None:
            print "Hello", name+"!"
        else:
            print "Hello!"
    #Question: can we inherit private method
    def __update(self):
        print "Calling an update function!"
#Inheritance
class Graduate(Student):
    ##Question: how to inherit private variable
    def __init__(self, name, sid):
        super(Graduate, self).__init__(name, sid)
        self.gid = sid
#        print self.gid
#Polymorphism
#deferent type but have same function with different implementation
##with function
class dog():
    def sound(self):
        print "woof woof"
class bear:
    def sound(self):
        print "Groarr"
def makeSound(animalObj):
    animalObj.sound()
dogObj = dog()
bearObj = bear()
makeSound(dogObj)
makeSound(bearObj)

#Polymorphism with abstract class -- most commonly used
 
#Create virtual objects
james = Student("James", "880106 - 0")
emily = Graduate("Emily", "880106-3622")
david = Student("David", "880106 - 2")
david.printClassName()
print emily.gid

class Point(object):
    def __init__(self, x=None, y=None):
        if x is None and y is None:
            self.xCoord = 0
            self.yCoord = 0
        else:
            self.xCoord = x
            self.yCoord = y
    def __str__(self):
        return "(", self.xCoord, ",", self.yCoord, ")"
    def getX(self):
        return self.xCoord
    def getY(self):
        return self.yCoord
    def shift(self, xInc, yInc):
        self.xCoord = self.xCoord + xInc
        self.yCoord = self.yCoord + yInc
