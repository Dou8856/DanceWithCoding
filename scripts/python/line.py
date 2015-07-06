#Object composition: objects are composed of other objects
# A Line line is composed of two Point objects
import math

class Line:
    def __init__(self, fromPoint, toPoint): #set the default value
        self.startPoint = fromPoint
        self.endPoint = toPoint

    def length(self):
        diff_x = self.endPoint.getX() - self.startPoint.getX()
        diff_y = self.endPoint.getY() - self.startPoint.getY()
        return math.sqrt(diff_x * diff_x + diff_y * diff_y)

    def getStartPoint(self):
        return self.startPoint()

    def getEndPoint(self):
        return self.endPoint()

    def isHorizontal(self):
        if self.startPoint != self.endPoint:
            return self.startPoint.getX() == self.endPoint.getY()
        else:
                return false


    def isVertical(self):
        return self.startPoint.getY() == self.endPoint.getY()
        
    def slop(self):
        if not self.isVertical():
            run = self.startPoint.getX() - self.endPoint.getX()
            rise = self.startPoint.getY() - self.endPoint.getY()
            return rise/float(run)
        else:
            return None
        

    




