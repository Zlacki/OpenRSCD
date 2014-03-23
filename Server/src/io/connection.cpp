#include "connection.hpp"
#include <boost/bind.hpp>
#include <algorithm>
#include "../model/collections.hpp"
#include "packethandler.hpp"
#include "../util.hpp"

namespace RSC {
	extern std::shared_ptr<Collections> collections;
	namespace IO {
		connection::connection(boost::asio::io_service& io_service)
			: socket_(io_service) {
		}

		boost::asio::ip::tcp::socket& connection::socket()
		{
			return socket_;
		}

		void connection::start()
		{
			handle_read();
		}


		void connection::handle_read() {
			char *buffer_ = new char[10000];
			auto self(shared_from_this());
			socket_.async_read_some(boost::asio::buffer(buffer_, 10000),
				[this, self, buffer_](boost::system::error_code e, std::size_t bytes_transferred) {
				if (!e) {
					while(bytes_transferred >= 2) {
						uint16_t length = ((buffer_[0] & 0xFF) << 8 | (buffer_[1] & 0xFF));
						if((int32_t) (bytes_transferred - length + 2) < 0) {
							break;
						}
						bytes_transferred -= length + 2;
						length--;
						if(bytes_transferred - 2 >= length) {
							auto opcode = buffer_[2] & 0xFF;
							std::shared_ptr<Packet> p = std::make_shared<Packet>(opcode);
							p->setLength(length);
							std::memcpy(p->getBuffer(), buffer_ + 2, length);
							(*handlePacket[opcode])(collections->getPlayer(getIndex()), p);
						}
					}
					handle_read();
				} else {
					/* TODO: Cleanup player object */
					warning("ASIO socket read error");
					socket_.close();
				}
			});
		}

		void connection::handle_write(char *data, std::size_t length)
		{
			auto self(shared_from_this());
			boost::asio::async_write(socket_, boost::asio::buffer(data, length),
				[this, self](boost::system::error_code ec, std::size_t length) {
				if (!ec) {  }
			});
		}

		void connection::setIndex(int index) {
			this->index = index;
		}

		int connection::getIndex() {
			return index;
		}
	}
}
