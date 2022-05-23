from http import HTTPStatus
import requests



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

    http_response = requests.post(
        "http://localhost:8081/agentSignIn", json={"agentId": 201})

    if(http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail4'

    http_response = requests.post(
        "http://localhost:8081/agentSignIn", json={"agentId": 202})

    if (http_response.status_code != HTTPStatus.CREATED):
        test_result = 'Fail5'

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

    http_response = requests.get(f"http://localhost:8081/order/{order_id}")

    if(http_response.status_code != HTTPStatus.OK):
        test_result = 'Fail8'

    res_body = http_response.json()

    agent_id = res_body.get("agentId")
    order_status = res_body.get("status")
    actual_order_id = res_body.get("orderId")

    if(agent_id != 201 or order_status != 'assigned' or actual_order_id != order_id):
        test_result = 'Fail9'

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
