from http import HTTPStatus
import requests

# reInitialize all the services
# make 3 agents in available state by making them sign in
# create a new order
# Check only the lowest id agent must be unavailable and rest agents must be available to take orders.


# RESTAURANT SERVICE    : http://localhost:8080
# DELIVERY SERVICE      : http://localhost:8081
# WALLET SERVICE        : http://localhost:8082

def test():
    test_result = 'Pass'

    # Reinitialize Restaurant service
    http_response = requests.post("http://localhost:8080/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail'

    # Reinitialize Delivery service
    http_response = requests.post("http://localhost:8081/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail'

    # Reinitialize Wallet service
    http_response = requests.post("http://localhost:8082/reInitialize")

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail'

    # Agent 201 sign in
    http_response = requests.post(
        "http://localhost:8081/agentSignIn", json={"agentId": 201})

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail'

    # Check Agent 201 status
    http_response = requests.get("http://localhost:8081/agent/201")

    if(http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail'

    res_body = http_response.json()

    agent_id = res_body.get("agentId")
    status = res_body.get("status")

    if agent_id != 201 or status != "available":
        test_result = 'Fail'

        ##################################################3333
        # Agent 202 sign in
    http_response = requests.post(
        "http://localhost:8081/agentSignIn", json={"agentId": 202})

    if (http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail'

    # Check Agent 202 status
    http_response = requests.get("http://localhost:8081/agent/202")

    if (http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail'

    res_body = http_response.json()

    agent_id = res_body.get("agentId")
    status = res_body.get("status")

    if agent_id != 202 or status != "available":
        test_result = 'Fail'
        ##################################################
        # Agent 203 sign in
    http_response = requests.post(
        "http://localhost:8081/agentSignIn", json={"agentId": 203})

    if (http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail'

    # Check Agent 203 status
    http_response = requests.get("http://localhost:8081/agent/203")

    if (http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail'

    res_body = http_response.json()

    agent_id = res_body.get("agentId")
    status = res_body.get("status")

    if agent_id != 203 or status != "available":
        test_result = 'Fail'


    # Customer 301 requests an order of item 1, quantity 3 from restaurant 101
    http_response = requests.post(
        "http://localhost:8081/requestOrder", json={"custId": 301, "restId": 101, "itemId": 1, "qty": 3})

    res_body = http_response.json()
    order_id = -1
    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail'
    else:
        order_id = res_body.get("orderId")

    if (order_id == -1):
        test_result = 'Fail'

    # Check Agent 201 status
    http_response = requests.get("http://localhost:8081/agent/201")

    if(http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail'

    res_body = http_response.json()

    agent_id = res_body.get("agentId")
    status = res_body.get("status")

    if agent_id != 201 or status != "unavailable":
        test_result = 'Fail'

    ########################################################33

        # Check Agent 202 status
        http_response = requests.get("http://localhost:8081/agent/202")

        if (http_response.status_code != HTTPStatus.OK):
            test_result = 'Fail'

        res_body = http_response.json()

        agent_id = res_body.get("agentId")
        status = res_body.get("status")

        if agent_id != 202 or status != "available":
            test_result = 'Fail'

        ###############################################################
            # Check Agent 203 status
            http_response = requests.get("http://localhost:8081/agent/203")

            if (http_response.status_code != HTTPStatus.OK):
                test_result = 'Fail'

            res_body = http_response.json()

            agent_id = res_body.get("agentId")
            status = res_body.get("status")

            if agent_id != 203 or status != "available":
                test_result = 'Fail'



    #
    # # Mark order $order_id as delivered
    # http_response = requests.post(
    #     "http://localhost:8081/orderDelivered", json={"orderId": order_id})
    #
    # # Check Order status
    # http_response = requests.get(f"http://localhost:8081/order/{order_id}")
    #
    # if(http_response.status_code != HTTPStatus.OK):
    #     test_result = 'Fail'
    #
    # res_body = http_response.json()
    #
    # agent_id = res_body.get("agentId")
    # order_status = res_body.get("status")
    # actual_order_id = res_body.get("orderId")
    #
    # if(agent_id != 201 or order_status != 'delivered' or actual_order_id != order_id):
    #     test_result = 'Fail'

    return test_result


if __name__ == "__main__":
    test_result = test()
    print(test_result)
