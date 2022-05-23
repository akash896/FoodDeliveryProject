from http import HTTPStatus
import requests



    # Reinitialize Restaurant service
    http_response = requests.post("http://localhost:8080/reInitialize")

    if (http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail1'

    # Reinitialize Delivery service
    http_response = requests.post("http://localhost:8081/reInitialize")

    if (http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail2'

    # Reinitialize Wallet service
    http_response = requests.post("http://localhost:8082/reInitialize")

    if (http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail3'

    # Agent 201 sign in
    http_response = requests.post(
        "http://localhost:8081/agentSignIn", json={"agentId": 201})

    if (http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail4'

    http_response = requests.get("http://localhost:8081/agent/201")

    if (http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail5'

    res_body = http_response.json()

    agent_id = res_body.get("agentId")
    status = res_body.get("status")

    if agent_id != 201 or status != "available":
        test_result = 'Fail6'

    http_response = requests.post(
        "http://localhost:8081/requestOrder", json={"custId": 301, "restId": 101, "itemId": 1, "qty": 3})

    res_body = http_response.json()
    order_id = -1
    if (http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail7'
    else:
        order_id = res_body.get("orderId")

    if (order_id != 1000):
        test_result = 'Fail8'

        http_response = requests.post(
            "http://localhost:8081/requestOrder", json={"custId": 301, "restId": 101, "itemId": 1, "qty": 3})

        res_body = http_response.json()
        order_id = -1
        if (http_response.status_code != HTTPStatus.CREATED):
            test_result = 'Fail9'
        else:
            order_id = res_body.get("orderId")

        if (order_id != 1001):
            test_result = 'Fail10'

    http_response = requests.get("http://localhost:8081/agent/201")

    if (http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail11'

    res_body = http_response.json()

    agent_id = res_body.get("agentId")
    status = res_body.get("status")

    if agent_id != 201 or status != "unavailable":
        test_result = 'Fail12'

    http_response = requests.post(
        "http://localhost:8081/orderDelivered", json={"orderId": order_id})


    http_response = requests.get(f"http://localhost:8081/order/{order_id}")

    if(http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail13'

    res_body = http_response.json()

    agent_id = res_body.get("agentId")
    order_status = res_body.get("status")
    actual_order_id = res_body.get("orderId")

    if(agent_id != 201 or order_status != 'delivered' or actual_order_id != order_id):
        test_result = 'Fail'

        http_response = requests.get("http://localhost:8081/agent/201")

        if (http_response.status_code != HTTPStatus.OK):
            test_result = 'Fail14'

        res_body = http_response.json()

        agent_id = res_body.get("agentId")
        status = res_body.get("status")

        if agent_id != 201 or status != "unavailable":
            test_result = 'Fail15'

    return test_result


if __name__ == "__main__":
    test_result = test()
    print(test_result)
