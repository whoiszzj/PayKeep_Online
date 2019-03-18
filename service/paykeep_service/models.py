from django.db import models
class Pay(models.Model):
    id = models.IntegerField(null=False, primary_key=True)
    item_spend = models.CharField(null=False, max_length=50)
    money = models.FloatField(null=False)
    year = models.IntegerField(null=False)
    month = models.IntegerField(null=False)
    day = models.IntegerField(null=False)
    isPri = models.BooleanField(null=False)
    data_added = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        temp = "id is : " + str(self.id) + "--item is : " + str(self.item_spend)
        return temp

# Create your models here.
