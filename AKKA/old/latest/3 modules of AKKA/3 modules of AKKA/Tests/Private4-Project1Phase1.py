# /**
#  * @author Jayesh Malviya
#  * @email thatsjm4u@rediffmail.com
#  * @create date 2022-03-04 11:32:32
#  * @modify date 2022-03-04 11:32:32
#  * @desc [description]
#  */
 
from http import HTTPStatus
import requests

# Checks weather basic flow works  or not?

#  1. ReInitialize,
#  2. All Agents-Online
#  3. Bulk Orders until Restaurant Items empty 
#  4. Order Delivery APIs
#  5. check for previous orders
#  6. check for Delivery Agents Status
#  7. Agents SignOut 
#  8. refill items
#  9. reoder
# 10. check order status
# 11. make single agent online
# 12. check is able to clear all items?
# 13. Sing-out Agent

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

    if delivery.reInitialize(1) : test_result = 'Fail'

    if restaurant.reInitialize(2) : test_result = 'Fail'
    
    if wallet.reInitialize(3) : test_result = 'Fail'
    

    # All Agent sign in
    if delivery.agentSignIn(4, 201) : test_result = 'Fail'
    
    if delivery.agentSignIn(5, 15) : test_result = 'Fail'
    
    if delivery.agentSignIn(6, 203) : test_result = 'Fail'
    
    if delivery.agentSignIn(7, 10) : test_result = 'Fail'
    

    # Bulk Orders 8 Orders (0-7)
    if delivery.requestOrder(8, 301, 101, 1, 10, 'order_accepted') : test_result = 'Fail'
    
    if delivery.requestOrder(9, 302, 101, 2, 10, 'order_gone') : test_result = 'Fail'
    
    if delivery.requestOrder(10, 303, 101, 1, 10, 'order_gone') : test_result = 'Fail'
    
    if delivery.requestOrder(11, 303, 102, 1, 10, 'order_accepted') : test_result = 'Fail'
    
    if delivery.requestOrder(12, 302, 102, 3, 20, 'order_accepted') : test_result = 'Fail'
    
    if delivery.requestOrder(13, 303, 102, 4, 5, 'order_accepted') : test_result = 'Fail'
    
    if delivery.requestOrder(14, 301, 102, 4, 4, 'order_accepted') : test_result = 'Fail'
    
    if delivery.requestOrder(15, 302, 102, 4, 6, 'order_accepted') : test_result = 'Fail'
    
    if delivery.agentStatus(16, 10, 'unavailable') : test_result = 'Fail'
    
    if delivery.agentStatus(17, 15, 'unavailable') : test_result = 'Fail'
    
    if delivery.agentStatus(18, 201, 'unavailable') : test_result = 'Fail'
    
    if delivery.agentStatus(19, 203, 'unavailable') : test_result = 'Fail'
    


    # Order Delivery API
    # Starting 4 Orders Delivered
    if delivery.orderStatus(20, 1004, 'unassigned') : test_result = 'Fail'
    
    if delivery.orderDelivered(21, 1000) : test_result = 'Fail'
    
    if delivery.orderStatus(22, 1005,'unassigned') : test_result = 'Fail'
    
    if delivery.orderDelivered(23, 1001) : test_result = 'Fail'
    
    if delivery.orderDelivered(24, 1002) : test_result = 'Fail'
    
    if delivery.orderDelivered(25, 1003) : test_result = 'Fail'
    

    # check for previous orders
    if delivery.orderStatus(26, 1004, 'assigned', 10) : test_result = 'Fail'
    
    if delivery.orderStatus(27, 1005, 'assigned', 15) : test_result = 'Fail'
    
    if delivery.orderStatus(28, 1002, 'delivered', 201) : test_result = 'Fail'
    
    if delivery.orderStatus(29, 1003, 'delivered', 203) : test_result = 'Fail'
    

    # check for Delivery Agents Status
    if delivery.agentStatus(30, 10, 'unavailable') : test_result = 'Fail'
    
    if delivery.agentStatus(31, 15, 'unavailable') : test_result = 'Fail'
    
    if delivery.agentStatus(32, 201, 'available') : test_result = 'Fail'
    
    if delivery.agentStatus(33, 203, 'available') : test_result = 'Fail'
    

    # Checking Delivery API & Agent Details
    if delivery.orderDelivered(34, 1004) : test_result = 'Fail'
    
    if delivery.agentStatus(35, 10, 'available') : test_result = 'Fail'
    
    if delivery.agentStatus(36, 15, 'unavailable') : test_result = 'Fail'
    
    if delivery.orderDelivered(37, 1005) : test_result = 'Fail'
    
    if delivery.agentStatus(38, 15, 'available') : test_result = 'Fail'
    

    # Order Stutus and Agent Status
    if delivery.orderStatus(39, 1000, 'delivered', 10) : test_result = 'Fail'
    
    if delivery.orderStatus(40, 1001, 'delivered', 15) : test_result = 'Fail'
    
    if delivery.orderStatus(41, 1002, 'delivered', 201) : test_result = 'Fail'
    
    if delivery.orderStatus(42, 1003, 'delivered', 203) : test_result = 'Fail'
    
    if delivery.orderStatus(43, 1004, 'delivered', 10) : test_result = 'Fail'
    
    if delivery.orderStatus(44, 1005, 'delivered', 15) : test_result = 'Fail'
    
    if delivery.agentStatus(45, 10, 'available') : test_result = 'Fail'
    
    if delivery.agentStatus(46, 15, 'available') : test_result = 'Fail'
    
    if delivery.agentStatus(47, 201, 'available') : test_result = 'Fail'
    
    if delivery.agentStatus(48, 203, 'available') : test_result = 'Fail'
    

    # Agents SignOut
    if delivery.agentSignOut(49, 10) : test_result = 'Fail'
    
    if delivery.agentStatus(50, 10, 'signed-out') : test_result = 'Fail'
    
    if delivery.agentSignOut(51, 15) : test_result = 'Fail'
    
    if delivery.agentStatus(52, 15, 'signed-out') : test_result = 'Fail'
    
    if delivery.agentStatus(53, 201, 'available') : test_result = 'Fail'
    
    if delivery.agentSignOut(54, 201) : test_result = 'Fail'
    
    if delivery.agentSignOut(55, 203) : test_result = 'Fail'
    
    if delivery.agentStatus(56, 201, 'signed-out') : test_result = 'Fail'
    
    if delivery.agentStatus(57, 203, 'signed-out') : test_result = 'Fail'
    


    # Refill Items

    if delivery.agentSignIn(58, 201) : test_result = 'Fail'
    
    if restaurant.refillItem(59, 101, 1, 10) : test_result = 'Fail'

    if restaurant.refillItem(60, 101, 2, 10) : test_result = 'Fail'

    if delivery.requestOrder(61, 302, 101, 2, 10, 'insufficient_fund') : test_result = 'Fail'

    if delivery.agentStatus(62, 201, 'available') : test_result = 'Fail'

    if delivery.requestOrder(63, 302, 101, 2, 1, 'order_accepted') : test_result = 'Fail'
    
    if delivery.agentStatus(64, 201, 'unavailable') : test_result = 'Fail'

    if delivery.orderStatus(65, 1006, 'assigned', 201) : test_result = 'Fail'

    if delivery.orderDelivered(66, 1006) : test_result = 'Fail'

    if delivery.agentStatus(67, 201, 'available') : test_result = 'Fail'

    if delivery.requestOrder(68, 303, 101, 1, 10, 'insufficient_fund') : test_result = 'Fail'

    if delivery.requestOrder(69, 303, 101, 1, 1, 'order_accepted') : test_result = 'Fail'

    if delivery.orderStatus(70, 1006, 'delivered', 201) : test_result = 'Fail'

    if delivery.orderStatus(71, 1007, 'assigned', 201) : test_result = 'Fail'

    if delivery.agentStatus(72, 201, 'unavailable') : test_result = 'Fail'

    if delivery.agentStatus(73, 10, 'signed-out') : test_result = 'Fail'
    
    if delivery.orderDelivered(74, 1007) : test_result = 'Fail'

    if delivery.orderStatus(75, 1007, 'delivered', 201) : test_result = 'Fail'

    if delivery.agentStatus(76, 201, 'available') : test_result = 'Fail'

    if delivery.agentSignOut(77, 201) : test_result = 'Fail'

    if delivery.agentStatus(78, 201, 'signed-out') : test_result = 'Fail'

    return test_result

# Class Containes Delivery API's
class Delivery: 

    # ReInitialize Method for Delivery
    @staticmethod
    def reInitialize(i): 
        http_response = requests.post("http://localhost:8081/reInitialize")
        if http_response.status_code != HTTPStatus.CREATED:
            print(i, ". Delivery /reInitialize\033[1;40m FAIL \033[1;00m")
        else:
            print(i, ". Delivery /reInitialize\033[1;32m PASS \033[1;00m")

        return http_response.status_code != HTTPStatus.CREATED

    # Delivery Agent agentSignIn
    @staticmethod
    def agentSignIn(i, agentId): 
        http_response = requests.post("http://localhost:8081/agentSignIn", json={"agentId": agentId})
        if http_response.status_code != HTTPStatus.CREATED:
            print(i, ". Delivery /agentSignIn\033[1;40m FAIL \033[1;00m")
        else: 
            print(i, ". Delivery /agentSignIn\033[1;32m PASS \033[1;00m")

        return http_response.status_code != HTTPStatus.CREATED

    # Delivery Agent SignOut
    @staticmethod
    def agentSignOut(i, agentId): 
        http_response = requests.post("http://localhost:8081/agentSignOut", json={"agentId": agentId})
        if http_response.status_code != HTTPStatus.CREATED:
            print(i, ". Delivery /agentSignOut\033[1;40m FAIL \033[1;00m")
        else:
            print(i, ". Delivery /agentSignOut\033[1;32m PASS \033[1;00m")

        return (http_response.status_code != HTTPStatus.CREATED)

    # Delivery Agent Status : { available, unavailable, sign-out }
    @staticmethod
    def agentStatus(i, agentId, status):
        http_response = requests.get("http://localhost:8081/agent/" + str(agentId))
        if(http_response.status_code != HTTPStatus.OK): 
            print(i, ". Delivery /agent\033[1;40m FAIL \033[1;00m")
            return True

        res_body = http_response.json()
        agent_id = res_body.get("agentId")
        agent_status = res_body.get("status")
        # print ("Agent : ", res_body)
        if (agent_id != agentId or agent_status != status): 
            print(i, ". Delivery /agent\033[1;40m FAIL \033[1;00m")
        else:
            print(i, ". Delivery /agent\033[1;32m PASS \033[1;00m")

        return (agent_id != agentId or agent_status != status)

    # Requesting order API call by Customer
    @staticmethod
    def requestOrder(i, custId, restId, itemId, qty, _case):
        http_response = requests.post(
        "http://localhost:8081/requestOrder", json={"custId": custId, "restId": restId, "itemId": itemId, "qty": qty})

        res_body = http_response.json()
        order_id = -1

        if (_case == 'order_accepted') :
                if(http_response.status_code != HTTPStatus.CREATED):
                    print(i, ". Delivery /requestOrder\033[1;40m FAIL \033[1;00m")
                    return True
                else:
                    order_id = res_body.get("orderId")

                if (order_id == -1):
                    print(i, ". Delivery /requestOrder\033[1;40m FAIL \033[1;00m")
                    return True
                else:
                    print(i, ". Delivery /requestOrder\033[1;32m PASS \033[1;00m")
                    return False
        else:
                if (http_response.status_code != 410):
                    print(i, ". Delivery /requestOrder\033[1;40m FAIL \033[1;00m")
                    return True
                else: 
                    print(i, ". Delivery /requestOrder\033[1;32m PASS \033[1;00m")
                    return False


    # Update Order status to Delivered 
    @staticmethod
    def orderDelivered(i, orderId):
        http_response = requests.post(
        "http://localhost:8081/orderDelivered", json={"orderId": orderId})
        if (http_response.status_code != HTTPStatus.CREATED): 
            print(i, ". Delivery /orderDelivered\033[1;40m FAIL \033[1;00m")
            return True
        else:
            print(i, ". Delivery /orderDelivered\033[1;32m PASS \033[1;00m")
            return False

    # Order Status
    @staticmethod
    def orderStatus(i, orderId, status, agentId = -1):
        http_response = requests.get("http://localhost:8081/order/" + str(orderId) )

        if(http_response.status_code != HTTPStatus.OK): 
            print(i, ". Delivery /order\033[1;40m FAIL \033[1;00m")
            return True
        
        res_body = http_response.json()
        agent_id = res_body.get("agentId")
        order_status = res_body.get("status")
        actual_order_id = res_body.get("orderId")

        # print (order_status, status, actual_order_id, orderId, agent_id, agentId)
        if (order_status != status or actual_order_id != orderId or agent_id != agentId):
            print(i, ". Delivery /order\033[1;40m FAIL \033[1;00m")
            return True
        else:
            print(i, ". Delivery /order\033[1;32m PASS \033[1;00m")
            return False
    
#  Restaurant Class Contains Restaurant API's
class Restaurant:
        @staticmethod
        def reInitialize(i):
            http_response = requests.post("http://localhost:8080/reInitialize")
            if (http_response.status_code != HTTPStatus.CREATED):
                print(i, ". Restaurant /reInitialize\033[1;40m FAIL \033[1;00m")
            else:
                print(i, ". Restaurant /reInitialize\033[1;32m PASS \033[1;00m")

            return http_response.status_code != HTTPStatus.CREATED

        def refillItem(k, i, restId, itemId, qty):
            http_response = requests.post("http://localhost:8080/refillItem", json = {"restId": restId, "itemId": itemId, "qty": qty })
            if (http_response.status_code != HTTPStatus.CREATED): 
                print(i, ". Restaurant /refillItem \033[1;40m FAIL \033[1;00m")
            else:
                print(i, ". Restaurant /refillItem \033[1;32m PASS \033[1;00m")

            return http_response.status_code != HTTPStatus.CREATED
        
# Wallet Class Contains Wallet API's
class Wallet:
        @staticmethod
        def reInitialize(i):
            http_response = requests.post("http://localhost:8082/reInitialize")
            if (http_response.status_code != HTTPStatus.CREATED): 
                print(i, ". Wallet /reInitialize\033[1;40m FAIL \033[1;00m")
            else:
                print(i, ". Wallet /reInitialize\033[1;32m PASS \033[1;00m")

            return (http_response.status_code != HTTPStatus.CREATED)


if __name__ == "__main__":
    test_result = test()
    print(test_result)
