import random,sys
from io import StringIO
def getRandomNameWithGender():
    family_names = "赵钱孙李周吴郑王"
    names_man = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山全国胜学祥才发武新利清"
    names_woman= "雪荣爱霞香月莺媛艳瑞凡佳嘉琼勤珍瑾颖露瑶慧巧美娜静"
    len_ = random.randint(1,2)
    gender = random.randint(0,1)
    name = random.choice(family_names)
    for _ in range(0,len_):
        name += random.choice(names_man if gender == 0 else names_woman)
    return "男" if gender == 0 else "女",name
def getRandomPhone():
    return int(1e10)+random.randrange(int(2e9),int(1e10))
def getRandomProvince():
    provinces = ("河北","河南","湖北","湖南","江苏","江西","山东","陕西","山西","广东","浙江","福建","辽宁","吉林","黑龙江","甘肃","青海","云南","海南","四川")
    return random.choice(provinces)
if __name__ == "__main__":
    fp = open("test_stu.sql","w",encoding="utf-8",newline='\n')
    fp.write("-- 任何个人信息如有雷同，纯属巧合\n")
    if len(sys.argv) == 1 or sys.argv[1] == "-d":
        fp.write("delete from student where id>=2010;\nALTER TABLE student AUTO_INCREMENT = 2010;\n")
    elif sys.argv[1] != "-nd":
        raise Exception("参数错误：-d表示删除2010以后的学生，-nd表示不删除")
    for i in range(0,30):
        string = StringIO(newline='\n')
        string.write("insert into student(sname, gender, address, age, phone) values(")
        a = getRandomNameWithGender()
        string.write("'{}',".format(a[1]))
        string.write("'{}',".format(a[0]))
        string.write("'{}省xx市xx区',".format(getRandomProvince()))
        string.write(str(random.randint(18,25))+',')
        string.write(str(getRandomPhone()))
        string.write(');\n')
        fp.write(string.getvalue())
        string.close()
    fp.close()