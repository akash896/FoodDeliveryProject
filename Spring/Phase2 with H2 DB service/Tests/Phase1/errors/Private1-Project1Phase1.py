# /**
#  * @author Jayesh Malviya
#  * @email thatsjm4u@rediffmail.com
#  * @create date 2022-03-30 17:46:30
#  * @modify date 2022-03-30 17:46:30
#  * @desc [description]
#  */

from http import HTTPStatus
import requests
import time

# Checks weather basic flow works  or not?

#  0. We have to Test /reinitialize /requestOrder /getOrder
#  1. ReInitialize,
#  2. Check /getOrder for some invalid orders
#   Inbetween checking the corresponding Wallet balance.
#  3. Check for /requestOrder
#  4. Check /getOrder for above orders
#  5. Check /requestOrder for Item which are no more available
#  6. Check /getOrder for all above orders
#  7. Check /requestOrder for Empty Wallets
#  8. Check /getOrder for all above orders

# RESTAURANT SERVICE    : http://localhost:8080
# DELIVERY SERVICE      : http://localhost:8081
# WALLET SERVICE        : http://localhost:8082

# restId 101 {1 180 10} {2 230 20} 102{1 50 10} {3 60 20} {4 45 15}
# agentId 201 10 15 203
# custId 301 302 303
# Balance 2000 

def test():
    test_result = 'Pass'

    # Reinitialize Restaurant service
   
    delivery = Delivery()
    restaurant = Restaurant()
    wallet = Wallet()

    if delivery.reInitialize(1): test_result = 'Fail'

    if restaurant.reInitialize(2): test_result = 'Fail'
    
    if wallet.reInitialize(3): test_result = 'Fail'
    
    if delivery.orderStatus(4, 1000, 'unassigned', HTTPStatus.NOT_FOUND): test_result = 'Fail'

    if delivery.requestOrder(5, 301, 101, 1, 10, 'order_accepted'): test_result = 'Fail'

    if delivery.orderStatus(6, 1000, 'delivered', HTTPStatus.OK): test_result = 'Fail'
    
    if wallet.getBalance(7, 301, 200): test_result = 'Fail'
    
    if delivery.requestOrder(8, 302, 101, 2, 10, 'order_accepted'): test_result = 'Fail'
    
    # if delivery.requestOrder(9, 303, 101, 1, 10, 'order_gone'): test_result = 'Fail'
    if delivery.requestOrder(9, 303, 101, 1, 10, 'order_accepted'): test_result = 'Fail'

    if delivery.requestOrder(10, 302, 102, 3, 20, 'order_accepted') : test_result = 'Fail'

    if delivery.requestOrder(11, 303, 102, 1, 10, 'order_accepted'): test_result = 'Fail'

    if delivery.requestOrder(12, 302, 102, 4, 6, 'order_accepted') : test_result = 'Fail'
    # if delivery.requestOrder(13, 302, 101, 2, 10, 'insufficient_fund') : test_result = 'Fail'
    if delivery.requestOrder(13, 302, 101, 2, 10, 'order_accepted') : test_result = 'Fail'
    
    
    return test_result


# Class Containes Delivery API's
class Delivery: 

    # Checks the correctness of the /reinitialize API, the input parameter i shows the index of the API,
    # so that we can easliy track the pass or failed API's!
    @staticmethod
    def reInitialize(i): 
        http_response = requests.post("http://localhost:8081/reInitialize")
        # print(http_response)
        if http_response.status_code != HTTPStatus.CREATED:
            print(i, ". Delivery /reInitialize\033[1;40m \t\t\t FAIL \033[1;00m")
        else:
            print(i, ". Delivery /reInitialize\033[1;32m \t\t\t PASS \033[1;00m")

        return http_response.status_code != HTTPStatus.CREATED

    # Checks the correctness of the /agentSignIn API, 
    # i: shows the index of the API called/Executed, so that we can easliy track the pass or failed API's!
    # agentId: of agent willing to SingIn.
    @staticmethod
    def agentSignIn(i, agentId):
        http_response = requests.post("http://localhost:8081/agentSignIn", json={"agentId": agentId})
        if http_response.status_code != HTTPStatus.CREATED:
            print(i, ". Delivery /agentSignIn\033[1;40m \t\t\t FAIL \033[1;00m")
        else: 
            print(i, ". Delivery /agentSignIn\033[1;32m \t\t\t PASS \033[1;00m")

        return http_response.status_code != HTTPStatus.CREATED

    # Checks the correctness of the /agentSignOut API, 
    # i: shows the index of the API called/Executed, so that we can easliy track the pass or failed API's!
    # agentId: of agent who is willing to sign-out. sign_out_api
    @staticmethod
    def agentSignOut(i, agentId): 
        http_response = requests.post("http://localhost:8081/agentSignOut", json={"agentId": agentId})
        if http_response.status_code != HTTPStatus.CREATED:
            print(i, ". Delivery /agentSignOut\033[1;40m \t\t\t FAIL \033[1;00m")
        else:
            print(i, ". Delivery /agentSignOut\033[1;32m \t\t\t PASS \033[1;00m")

        return (http_response.status_code != HTTPStatus.CREATED)
    
    # Checks the correctness of the /agentStatus API, 
    # i: shows the index of the API called/Executed, so that we can easliy track the pass or failed API's!
    # agentId: get agent Status of.
    # status: expected status of the agent (identified by agentId)
    # Possible Delivery Agent Status : { available, unavailable, sign-out }
    @staticmethod
    def agentStatus(i, agentId, status):
        http_response = requests.get("http://localhost:8081/agent/" + str(agentId))
        if(http_response.status_code != HTTPStatus.OK): 
            print(i, ". Delivery /agent\033[1;40m \t\t\t FAIL \033[1;00m")
            return True

        res_body = http_response.json()
        agent_id = res_body.get("agentId")
        agent_status = res_body.get("status")
        # print ("Agent : ", res_body)
        if (agent_id != agentId or agent_status != status): 
            print(i, ". Delivery /agent\033[1;40m \t\t\t FAIL \033[1;00m")
        else:
            print(i, ". Delivery /agent\033[1;32m \t\t\t PASS \033[1;00m")

        return (agent_id != agentId or agent_status != status)

    # Checks the correctness of the /requestOrder API, 
    # i: shows the index of the API called/Executed, so that we can easliy track the pass or failed API's!
    # custId: of Customer who requested the order.
    # restId: of Restaurant from where the Customer wants to order.
    # itemId: of item which s/he selected to order from Restaurant restId.
    # qty: Specifiy the quantity of the item.
    # _case: Cases when the `Order_accepted` and other case when order_not_accepted by 
        # `Insufficent_Fund` or `Other_Server_Not_Up` or `Not_Enough_Quantity_In_Restaurant` or `Any_Other_Case`
        # We combined all this other category into one as all returning the same value.
    @staticmethod
    def requestOrder(i, custId, restId, itemId, qty, _case):
        time.sleep(2)
        http_response = requests.post(
        "http://localhost:8081/requestOrder", json={"custId": custId, "restId": restId, "itemId": itemId, "qty": qty})

        # print(http_response)
        # print(http_response.json())
        res_body = http_response.json()
        order_id = -1

        if (_case == 'order_accepted') :
                if(http_response.status_code != HTTPStatus.CREATED):
                    print(i, ". Delivery /requestOrder\033[1;40m \t\t\t FAIL \033[1;00m")
                    return True
                else:
                    order_id = res_body.get("orderId")

                if (order_id == -1):
                    print(i, ". Delivery /requestOrder\033[1;40m \t\t\t FAIL \033[1;00m")
                    return True
                else:
                    print(i, ". Delivery /requestOrder\033[1;32m \t\t\t PASS \033[1;00m")
                    return False
        else:
                if (http_response.status_code != 410):
                    print(i, ". Delivery /requestOrder\033[1;40m \t\t\t FAIL \033[1;00m")
                    return True
                else: 
                    print(i, ". Delivery /requestOrder\033[1;32m \t\t\t PASS \033[1;00m")
                    return False
        time.sleep(1)


    
    # Checks the correctness of the /orderDelivered API, 
    # i: shows the index of the API called/Executed, so that we can easliy track the pass or failed API's!
    # orderId: of the item which was delivered.
    @staticmethod
    def orderDelivered(i, orderId):
        http_response = requests.post(
        "http://localhost:8081/orderDelivered", json={"orderId": orderId})
        if (http_response.status_code != HTTPStatus.CREATED): 
            print(i, ". Delivery /orderDelivered\033[1;40m \t\t\t FAIL \033[1;00m")
            return True
        else:
            print(i, ". Delivery /orderDelivered\033[1;32m \t\t\t PASS \033[1;00m")
            return False

    
    # Checks the correctness of the /order API, 
    # the input parameter i shows the index of the API,
    # so that we can easliy track the pass or failed API's!
    # oderId: which you are inquiring for,
    # orderStatus: expected status of the order,
    # status: http_response { HTTPStatus.OK, HTTPStatus.NOT_FOUND }
    # agentId: expected agentId, for first case we by pass checking the agentId, as its not returning.
    @staticmethod
    def orderStatus(i, orderId, orderStatus, status, agentId = -1):
        http_response = requests.get("http://localhost:8081/order/" + str(orderId) )

        # print(http_response)
        if (http_response.status_code == HTTPStatus.NOT_FOUND and status == HTTPStatus.NOT_FOUND):
            print(i, ". Delivery /orderStatus \033[1;32m \t\t\t PASS \033[1;00m")
            return False

        # print(http_response.status_code != HTTPStatus.OK and status == HTTPStatus.OK)
        if(http_response.status_code != HTTPStatus.OK and status == HTTPStatus.OK):
            print(i, ". Delivery /orderStatus \033[1;40m \t\t\t FAIL \033[1;00m")
            return True
                
        res_body = http_response.json()
        # agent_id = res_body.get("agentId")
        order_status = res_body.get("status")
        actual_order_id = res_body.get("orderId")

        # print(order_status != orderStatus)
        # print(actual_order_id != orderId)
        # print (order_status, orderStatus, status, actual_order_id, orderId) #, agent_id, agentId)
        if (order_status != orderStatus or actual_order_id != orderId): #or agent_id != agentId
            print(i, ". Delivery /orderStatus \033[1;40m \t\t\t FAIL \033[1;00m")
            return True 
        else:
            print(i, ". Delivery /orderStatus \033[1;32m \t\t\t PASS \033[1;00m")
            return False
    
#  Restaurant Class Contains Restaurant API's
class Restaurant:

    # Checks the correctness of the /reinitialize API, the input parameter i shows the index of the API,
    # so that we can easliy track the pass or failed API's!
        @staticmethod
        def reInitialize(i):
            http_response = requests.post("http://localhost:8080/reInitialize")
            if (http_response.status_code != HTTPStatus.CREATED):
                print(i, ". Restaurant /reInitialize\033[1;40m \t\t\t FAIL \033[1;00m")
            else:
                print(i, ". Restaurant /reInitialize\033[1;32m \t\t\t PASS \033[1;00m")

            return http_response.status_code != HTTPStatus.CREATED


    # Checks the correctness of the /refillItem API, the input parameter i shows the index of the API,
    # so that we can easliy track the pass or failed API's!
    # Refills the Item (identified by `itemId`) of the Restaurant (identified by `restId`) by given quantity (qty)
        def refillItem(i, restId, itemId, qty):
            http_response = requests.post("http://localhost:8080/refillItem", json = {"restId": resId, "itemId": itemId, "qty": qty })
            if (http_response.status_code != HTTPStatus.CREATED): 
                print(i, ". Restaurant /refillItem\033[1;40m \t\t\t FAIL \033[1;00m")
            else:
                print(i, ". Restaurant /refillItem\033[1;32m \t\t\t PASS \033[1;00m")

            return http_response.status_code != HTTPStatus.CREATED
        

# Wallet Class Contains Wallet API's
class Wallet:
    # Checks the correctness of the /reinitialize API, the input parameter i shows the index of the API,
    # so that we can easliy track the pass or failed API's!
        @staticmethod
        def reInitialize(i):
            http_response = requests.post("http://localhost:8082/reInitialize")
            if (http_response.status_code != HTTPStatus.CREATED): 
                print(i, ". Wallet /reInitialize\033[1;40m \t\t\t FAIL \033[1;00m")
            else:
                print(i, ". Wallet /reInitialize\033[1;32m \t\t\t PASS \033[1;00m")

            return (http_response.status_code != HTTPStatus.CREATED)

    # Checks the correctness of the /addBalance API, the input parameter i shows the index of the API,
    # so that we can easliy track the pass or failed API's!
    # custId: of Customer in which we are adding amount.
    # amount: amount to be added
        @staticmethod
        def addBalance(i, custId, amount):
            http_response = requests.post("http://localhost:8082/addBalance", json = {"custId": custId, "amount": amount})
            if (http_response.status_code != HTTPStatus.CREATED): 
                print(i, ". Wallet /reInitialize\033[1;40m \t\t\t FAIL \033[1;00m")
            else:
                print(i, ". Wallet /reInitialize\033[1;32m \t\t\t PASS \033[1;00m")

            return (http_response.status_code != HTTPStatus.CREATED)
    
    # Checks the correctness of the /deductBalance API, the input parameter i shows the index of the API,
    # so that we can easliy track the pass or failed API's!
    # custId: of Customer whose balance we are deducting.
    # amount: amount to be deducted
        @staticmethod
        def deductBalance(i, custId, amount):
            http_response = requests.post("http://localhost:8082/deductBalance",  json = {"custId": custId, "amount": amount})
            if (http_response.status_code != HTTPStatus.CREATED): 
                print(i, ". Wallet /deductBalance\033[1;40m \t\t\t FAIL \033[1;00m")
            else:
                print(i, ". Wallet /deductBalance\033[1;32m \t\t\t PASS \033[1;00m")

            return (http_response.status_code != HTTPStatus.CREATED)

    # Checks the correctness of the /balance/{id} API, the input parameter i shows the index of the API,
    # so that we can easliy track the pass or failed API's!
    # custId: of Customer whose balance we are checking.
    # balance: Expected Balance
        @staticmethod
        def getBalance(i, custId, balance):
            http_response = requests.get("http://localhost:8082/balance/" + str(custId))
            if (http_response.status_code != HTTPStatus.OK): 
                print(i, ". Wallet /balance\033[1;40m \t\t\t\t FAIL \033[1;00m")
                return True

            res_body = http_response.json()
            # agent_id = res_body.get("agentId")
            cust_Id = res_body.get("custId")
            bal = res_body.get("balance")
            
            if (cust_Id != custId or balance != int(bal)):
                print(i, ". Wallet /balance\033[1;40m \t\t\t\t FAIL \033[1;00m")
                return True
            else:
                print(i, ". Wallet /balance\033[1;32m \t\t\t\t PASS \033[1;00m")
                return False

if __name__ == "__main__":
    test_result = test()
    print(test_result)