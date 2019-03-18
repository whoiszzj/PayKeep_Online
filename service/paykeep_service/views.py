from django.shortcuts import render
from .models import Pay
from django.http import HttpResponse
from django.core.exceptions import ObjectDoesNotExist


def pay(request):
    if request.method == 'POST':
        item_spend = request.POST.get('item_spend')
        id = request.POST.get('id')
        money = request.POST.get('money')
        year = request.POST.get('year')
        month = request.POST.get('month')
        day = request.POST.get('day')
        isPri = request.POST.get('isPri')
        fun = request.POST.get('fun')
        if fun == '1':
            Pay.objects.create(id=id, item_spend=item_spend, money=money, year=year, month=month, day=day, isPri=isPri)
            try:
                t = Pay.objects.get(id=id)
                return HttpResponse("add_ok")
            except ObjectDoesNotExist:
                return HttpResponse("add_error")
        elif fun == '2':
            try:
                temp = Pay.objects.get(id=id)
                temp.delete()
                return HttpResponse("delete_ok")
            except ObjectDoesNotExist:
                return HttpResponse("not_found")
    else:
        return HttpResponse("error")

# Create your views here.

