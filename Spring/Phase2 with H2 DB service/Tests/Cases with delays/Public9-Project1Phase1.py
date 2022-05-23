from http import HTTPStatus
import requests

"""
When 2 agents are already signed-in (available) state. After that if 2 orders are requested, then the 
first order must be assigned to agent with lower agentId and 2nd order will be assigned to the agent with 
greater agentId 

# reInitialize all services
# make 2 agents(201, 202) as available
# create 2 orders
#check order details of 1st order, the agentId must be 201
#check order details of 2nd order, the agentId must be 202
"""


# RESTAURANT SERVICE    : http://localhost:8080
# DELIVERY SERVICE      : http://localhost:8081
# WALLET SERVICE        : http://localhost:8082

def test():
    test_result = 'Pass'


    # Reinitialize Restaurant service
    http_response = requests.post("http://localhost:8080/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail1'

    # Reinitialize Delivery service
    http_response = requests.post("http://localhost:8081/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail2'

    # Reinitialize Wallet service
    http_response = requests.post("http://localhost:8082/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail3'

    # Agent 201 sign in
    http_response = requests.post(
        "http://localhost:8081/agentSignIn", json={"agentId": 201})

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail4'

    # Agent 202 sign in
    http_response = requests.post(
        "http://localhost:8081/agentSignIn", json={"agentId": 202})

    if (http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail5'

    # Customer 301 requests an order of item 1, quantity 3 from restaurant 101
    http_response = requests.post(
        "http://localhost:8081/requestOrder", json={"custId": 301, "restId": 101, "itemId": 1, "qty": 3})

    res_body = http_response.json()
    order_id = -1
    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail6'
    else:
        order_id = res_body.get("orderId")

    if (order_id == -1):
        test_result = 'Fail7'

    # Check Order status
    http_response = requests.get(f"http://localhost:8081/order/{order_id}")

    if(http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail8'

    res_body = http_response.json()

    agent_id = res_body.get("agentId")
    order_status = res_body.get("status")
    actual_order_id = res_body.get("orderId")

    if(agent_id != 201 or order_status != 'assigned' or actual_order_id != order_id):
        test_result = 'Fail9'

    ################ 2nd order request ###########################################

    # Customer 301 requests an order of item 1, quantity 3 from restaurant 101
    http_response = requests.post(
        "http://localhost:8081/requestOrder", json={"custId": 301, "restId": 101, "itemId": 1, "qty": 3})

    res_body = http_response.json()
    order_id = -1
    if (http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail10'
    else:
        order_id = res_body.get("orderId")

    if (order_id == -1):
        test_result = 'Fail11'

    # Check Order status
    http_response = requests.get(f"http://localhost:8081/order/{order_id}")

    if (http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail12'

    res_body = http_response.json()

    agent_id = res_body.get("agentId")
    order_status = res_body.get("status")
    actual_order_id = res_body.get("orderId")

    if (agent_id != 202 or order_status != 'assigned' or actual_order_id != order_id):
        test_result = 'Fail13'


    return test_result


if __name__ == "__main__":
    test_result = test()
    print(test_result)
